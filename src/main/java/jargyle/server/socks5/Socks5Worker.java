package jargyle.server.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.client.DatagramSocketFactory;
import jargyle.client.ServerSocketFactory;
import jargyle.client.SocketFactory;
import jargyle.client.SocksClient;
import jargyle.common.net.DatagramPacketFilter;
import jargyle.common.net.FilterDatagramSocket;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.ClientMethodSelectionMessage;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.ServerMethodSelectionMessage;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.common.net.socks5.Version;
import jargyle.common.net.socks5.gssapiauth.GssDatagramPacketFilter;
import jargyle.common.net.socks5.gssapiauth.GssSocket;
import jargyle.common.util.PositiveInteger;
import jargyle.server.Address;
import jargyle.server.Configuration;
import jargyle.server.Criteria;
import jargyle.server.Criterion;
import jargyle.server.CriterionOperator;
import jargyle.server.Port;
import jargyle.server.PortRanges;
import jargyle.server.SettingSpec;
import jargyle.server.Settings;
import jargyle.server.SocksClients;
import jargyle.server.TcpRelayServer;

public final class Socks5Worker implements Runnable {

	private static final int HALF_SECOND = 500;
	
	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private Socket clientSocket;
	private final Configuration configuration;
	private final Logger logger;
	private final Settings settings;
	private final SocksClient socksClient;
	
	public Socks5Worker(
			final Socket clientSock, 
			final Configuration config, 
			final Logger lggr) {
		Settings sttngs = config.getSettings();
		SocksClient client = SocksClients.newSocksClient(config);
		this.clientInputStream = null;
		this.clientOutputStream = null;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.logger = lggr;
		this.settings = sttngs;
		this.socksClient = client;
	}
	
	private void doBind(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		PortRanges listenPortRanges = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_BIND_LISTEN_PORT_RANGES, 
				PortRanges.class);
		if (!listenPortRanges.contains(Port.newInstance(desiredDestinationPort))) {
			this.log(
					Level.WARNING, 
					"Invalid port for the listen socket");
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending: %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		ServerSocketFactory serverSocketFactory = 
				ServerSocketFactory.newInstance(this.socksClient);
		ServerSocket listenSocket = serverSocketFactory.newServerSocket();
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(listenSocket);
			listenSocket.bind(new InetSocketAddress(
					InetAddress.getByName(desiredDestinationAddress),
					desiredDestinationPort));
		} catch (IOException e) {
			this.log(
					Level.WARNING, 
					"Error in creating the listen socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		InetAddress inetAddress = listenSocket.getInetAddress();
		String serverBoundAddress =	inetAddress.getHostAddress();
		AddressType addressType = AddressType.get(serverBoundAddress);
		int serverBoundPort = listenSocket.getLocalPort();
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		this.log(
				Level.FINE, 
				String.format("Sending %s", socks5Rep.toString()));
		this.writeThenFlush(socks5Rep.toByteArray());
		Socket incomingSocket = null;
		try {
			incomingSocket = listenSocket.accept();
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_INCOMING_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(incomingSocket);
		} catch (SocketException e) {
			this.log(
					Level.WARNING, 
					"Error in setting the incoming socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		} catch (IOException e) {
			this.log(
					Level.WARNING, 
					"Error in waiting for an incoming socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		} finally {
			listenSocket.close();
			if (socks5Rep != null && socks5Rep.getReply().equals(
					Reply.GENERAL_SOCKS_SERVER_FAILURE)) {
				return;
			}
		}
		InetAddress incomingTcpInetAddress = incomingSocket.getInetAddress();
		String incomingTcpName = incomingTcpInetAddress.getHostName();
		String incomingTcpAddress = incomingTcpInetAddress.getHostAddress();
		Criteria allowedIncomingTcpAddressCriteria = 
				this.configuration.getAllowedIncomingTcpAddressCriteria();
		if (allowedIncomingTcpAddressCriteria.toList().isEmpty()) {
			allowedIncomingTcpAddressCriteria = Criteria.newInstance(
					CriterionOperator.MATCHES.newCriterion(".*"));
		}
		if (allowedIncomingTcpAddressCriteria.anyEvaluatesTrue(incomingTcpName) == null 
				&& allowedIncomingTcpAddressCriteria.anyEvaluatesTrue(incomingTcpAddress) == null) {
			this.log(
					Level.FINE, 
					String.format(
							"Incoming TCP address %s not allowed", 
							incomingTcpAddress));
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		Criteria blockedIncomingTcpAddressCriteria =
				this.configuration.getBlockedIncomingTcpAddressCriteria();
		Criterion criterion = 
				blockedIncomingTcpAddressCriteria.anyEvaluatesTrue(
						incomingTcpName);
		if (criterion != null) {
			this.log(
					Level.FINE, 
					String.format(
							"Incoming TCP address %s blocked based on the "
							+ "following criterion: %s", 
							incomingTcpName,
							criterion));
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		criterion = blockedIncomingTcpAddressCriteria.anyEvaluatesTrue(
				incomingTcpAddress);
		if (criterion != null) {
			this.log(
					Level.FINE, 
					String.format(
							"Incoming TCP address %s blocked based on the "
							+ "following criterion: %s", 
							incomingTcpAddress,
							criterion));
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		serverBoundAddress = incomingTcpAddress;
		addressType = AddressType.get(serverBoundAddress);
		serverBoundPort = incomingSocket.getLocalPort();
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		this.log(
				Level.FINE, 
				String.format("Sending %s", socks5Rep.toString()));
		this.writeThenFlush(socks5Rep.toByteArray());
		try {
			this.passData(
					incomingSocket, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (!incomingSocket.isClosed()) {
				incomingSocket.close();
			}
		}
	}
	
	private void doConnect(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		SocketFactory socketFactory = SocketFactory.newInstance(
				this.socksClient);
		Socket serverSocket = socketFactory.newSocket();
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverSocket);
			Address bindAddress = this.settings.getLastValue(
					SettingSpec.ADDRESS, Address.class);
			InetAddress bindInetAddress = bindAddress.toInetAddress();
			serverSocket.bind(new InetSocketAddress(
					bindInetAddress, 
					0));
			this.log(Level.INFO, String.format(
					"Binding to %s. Connecting to %s", 
					new InetSocketAddress(
							bindInetAddress, 
							0),
					new InetSocketAddress(
							InetAddress.getByName(desiredDestinationAddress),
							desiredDestinationPort)));
			int connectTimeout = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT, 
					PositiveInteger.class).intValue();
			serverSocket.connect(new InetSocketAddress(
					InetAddress.getByName(desiredDestinationAddress),
					desiredDestinationPort),
					connectTimeout);
			this.log(Level.INFO, String.format(
					"Bound to %s. Connected to %s", 
					serverSocket.getLocalSocketAddress(),
					serverSocket.getRemoteSocketAddress()));
		} catch (UnknownHostException e) {
			this.log(
					Level.WARNING, 
					"Error in creating the server-facing socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		} catch (IOException e) {
			this.log(
					Level.WARNING, 
					"Error in creating the server-facing socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		String serverBoundAddress = 
				serverSocket.getInetAddress().getHostAddress();
		AddressType addressType = AddressType.get(serverBoundAddress);
		int serverBoundPort = serverSocket.getPort();
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		this.log(
				Level.FINE, 
				String.format("Sending %s", socks5Rep.toString()));
		this.writeThenFlush(socks5Rep.toByteArray());
		try {
			this.passData(
					serverSocket, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(),
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (!serverSocket.isClosed()) {
				serverSocket.close();
			}
		}
	}
	
	private void doUdpAssociate(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		Address bindAddress = this.settings.getLastValue(
				SettingSpec.ADDRESS, Address.class);
		InetAddress bindInetAddress = bindAddress.toInetAddress();
		PortRanges serverPortRanges = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_PORT_RANGES, 
				PortRanges.class);
		Port serverPort = serverPortRanges.firstAvailableUdpPortAt(bindInetAddress);
		if (serverPort == null) {
			this.log(
					Level.WARNING, 
					"Unable to find available port for the server-facing UDP socket");
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		DatagramSocket serverDatagramSock = null;
		try {
			DatagramSocketFactory datagramSocketFactory = 
					DatagramSocketFactory.newInstance(this.socksClient);
			serverDatagramSock = datagramSocketFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, serverPort.intValue()));
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverDatagramSock);
		} catch (SocketException e) {
			this.log(
					Level.WARNING, 
					"Error in creating the server-facing UDP socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		PortRanges clientPortRanges = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_PORT_RANGES, 
				PortRanges.class);
		Port clientPort = clientPortRanges.firstAvailableUdpPortAt(bindInetAddress);
		if (clientPort == null) {
			this.log(
					Level.WARNING, 
					"Unable to find available port for the client-facing UDP socket");
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			if (!serverDatagramSock.isClosed()) {
				serverDatagramSock.close();
			}
			return;
		}
		DatagramSocket clientDatagramSock = null;
		try {
			if (this.clientSocket instanceof GssSocket) {
				GssSocket gssSocket = (GssSocket) this.clientSocket;
				DatagramPacketFilter datagramPacketFilter =
						new GssDatagramPacketFilter(
								gssSocket.getGSSContext(),
								gssSocket.getMessageProp());
				clientDatagramSock = new FilterDatagramSocket(
						datagramPacketFilter,
						new InetSocketAddress(
								bindInetAddress, clientPort.intValue()));
			} else if (!this.clientSocket.getClass().equals(Socket.class)) {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						Socket.class.getSimpleName(), 
						this.clientSocket.getClass().getSimpleName()));
			} else {
				clientDatagramSock = new DatagramSocket(new InetSocketAddress(
						bindInetAddress, clientPort.intValue()));
			}
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(clientDatagramSock);
		} catch (SocketException e) {
			this.log(
					Level.WARNING, 
					"Error in creating the client-facing UDP socket", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());
			if (!serverDatagramSock.isClosed()) {
				serverDatagramSock.close();
			}
			return;
		}
		InetAddress inetAddress = this.clientSocket.getLocalAddress();
		String serverBoundAddress = inetAddress.getHostAddress();
		AddressType addressType = AddressType.get(serverBoundAddress);
		int serverBoundPort = clientDatagramSock.getLocalPort();
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		this.log(
				Level.FINE, 
				String.format("Sending %s", socks5Rep.toString()));
		this.writeThenFlush(socks5Rep.toByteArray());
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientDatagramSock,
				serverDatagramSock,
				this.clientSocket.getInetAddress().getHostAddress(),
				desiredDestinationAddress,
				desiredDestinationPort, 
				this.configuration.getAllowedIncomingUdpAddressCriteria(), 
				this.configuration.getBlockedIncomingUdpAddressCriteria(), 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE, 
						PositiveInteger.class).intValue(), 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT, 
						PositiveInteger.class).intValue(), this.logger);
		try {
			udpRelayServer.start();
			while (!this.clientSocket.isClosed()
					&& !udpRelayServer.isStopped()) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch (IOException e) {
			this.log(
					Level.WARNING, 
					"Error in starting the UDP association", 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.log(
					Level.FINE, 
					String.format("Sending %s", socks5Rep.toString()));
			this.writeThenFlush(socks5Rep.toByteArray());				
		} finally {
			if (!udpRelayServer.isStopped()) {
				udpRelayServer.stop();
			}
			if (!clientDatagramSock.isClosed()) {
				clientDatagramSock.close();
			}
			if (!serverDatagramSock.isClosed()) {
				serverDatagramSock.close();
			}
		}
	}
	
	private void log(final Level level, final String message) {
		this.logger.log(
				level, 
				String.format("%s: %s",	this, message));
	}
	
	private void log(
			final Level level, final String message, final Throwable t) {
		this.logger.log(
				level, 
				String.format("%s: %s",	this, message),
				t);
	}
	
	private void passData(
			final Socket serverSocket, 
			final int bufferSize, 
			final int timeout) {
		TcpRelayServer tcpRelayServer = new TcpRelayServer(
				this.clientSocket, 
				serverSocket, 
				bufferSize, 
				timeout, 
				this.logger);
		try {
			tcpRelayServer.start();
			while (!tcpRelayServer.isStopped()) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch (IOException e) {
			this.log(
					Level.WARNING, 
					"Error in starting to pass data", 
					e);
		} finally {
			if (!tcpRelayServer.isStopped()) {
				tcpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			try {
				this.clientInputStream = this.clientSocket.getInputStream();
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in getting the input stream from the client", 
						e);
				return;
			}
			try {
				this.clientOutputStream = this.clientSocket.getOutputStream();
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in getting the output stream from the client", 
						e);
				return;
			}
			InputStream in = new SequenceInputStream(
					new ByteArrayInputStream(new byte[] { Version.V5.byteValue() }),
					this.clientInputStream);
			ClientMethodSelectionMessage cmsm = null;
			try {
				cmsm = ClientMethodSelectionMessage.newInstanceFrom(in); 
			} catch (IllegalArgumentException e) {
				this.log(
						Level.WARNING, 
						"Error in parsing the method selection message from the client", 
						e);
				return;
			}
			this.log(
					Level.FINE, 
					String.format("Received %s", cmsm.toString()));
			Method method = null;
			AuthMethods authMethods = this.settings.getLastValue(
					SettingSpec.SOCKS5_AUTH_METHODS, AuthMethods.class);
			for (AuthMethod authMethod : authMethods.toList()) {
				Method meth = authMethod.methodValue();
				if (cmsm.getMethods().contains(meth)) {
					method = meth;
					break;
				}
			}
			if (method == null) {
				method = Method.NO_ACCEPTABLE_METHODS;
			}
			ServerMethodSelectionMessage smsm = 
					ServerMethodSelectionMessage.newInstance(method);
			this.log(
					Level.FINE, 
					String.format("Sending %s", smsm.toString()));
			this.writeThenFlush(smsm.toByteArray());
			Authenticator authenticator = null;
			try {
				authenticator = Authenticator.valueOf(method);
			} catch (IllegalArgumentException e) {
				this.log(
						Level.WARNING, 
						String.format("Unhandled method: %s", method),
						e);
				return;
			}
			Socket socket = null;
			try {
				socket = authenticator.authenticate(
						this.clientSocket, this.configuration);
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in authenticating the client", 
						e);
				return;
			}
			this.clientInputStream = socket.getInputStream();
			this.clientOutputStream = socket.getOutputStream();
			this.clientSocket = socket;
			try {
				SocketSettings socketSettings = 
						this.settings.getLastValue(
								SettingSpec.CLIENT_SOCKET_SETTINGS,
								SocketSettings.class);
				socketSettings.applyTo(this.clientSocket);
			} catch (SocketException e) {
				this.log(
						Level.WARNING, 
						"Error in setting the client socket", 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.log(
						Level.FINE, 
						String.format("Sending %s", socks5Rep.toString()));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			}
			Socks5Request socks5Req = null;
			try {
				socks5Req = Socks5Request.newInstanceFrom(this.clientInputStream);
			} catch (IOException e) {
				this.log(
						Level.WARNING, 
						"Error in parsing the SOCKS5 request", 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.log(
						Level.FINE, 
						String.format("Sending %s", socks5Rep.toString()));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			}
			this.log(
					Level.FINE, 
					String.format("Received %s", socks5Req.toString()));
			Socks5RequestCriteria allowedSocks5RequestCriteria = 
					this.configuration.getAllowedSocks5RequestCriteria();
			if (allowedSocks5RequestCriteria.toList().isEmpty()) {
				allowedSocks5RequestCriteria = new Socks5RequestCriteria(
						new Socks5RequestCriterion(null, null, null));
			}
			if (allowedSocks5RequestCriteria.anyEvaluatesTrue(socks5Req) == null) {
				Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.log(
						Level.FINE, 
						String.format(
								"SOCKS5 request not allowed. SOCKS5 request: %s", 
								socks5Req.toString()));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			}
			Socks5RequestCriteria blockedSocks5RequestCriteria = 
					this.configuration.getBlockedSocks5RequestCriteria();
			Socks5RequestCriterion socks5RequestCriterion =
					blockedSocks5RequestCriteria.anyEvaluatesTrue(socks5Req);
			if (socks5RequestCriterion != null) {
				Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.log(
						Level.FINE, 
						String.format(
								"SOCKS5 request blocked based on the following "
								+ "criterion: %s. SOCKS5 request: %s",
								socks5RequestCriterion.toString(),
								socks5Req.toString()));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			}
			Command command = socks5Req.getCommand();
			switch (command) {
			case BIND:
				this.doBind(socks5Req);
				break;
			case CONNECT:
				this.doConnect(socks5Req);
				break;
			case UDP_ASSOCIATE:
				this.doUdpAssociate(socks5Req);
				break;
			default:
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						Command.class.getSimpleName(), 
						command));
			}
		} catch (Throwable t) {
			this.log(
					Level.WARNING, 
					"Internal server error", 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					this.log(
							Level.WARNING, 
							"Error upon closing connection to the client", 
							e);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientSocket=")
			.append(this.clientSocket)
			.append("]");
		return builder.toString();
	}

	private void writeThenFlush(final byte[] b) throws IOException {
		this.clientOutputStream.write(b);
		this.clientOutputStream.flush();
	}
	
}
