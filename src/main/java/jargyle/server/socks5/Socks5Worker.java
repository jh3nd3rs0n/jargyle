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
import jargyle.common.net.DatagramPacketFilterFactory;
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
import jargyle.common.util.PositiveInteger;
import jargyle.server.Configuration;
import jargyle.server.Criteria;
import jargyle.server.Criterion;
import jargyle.server.Host;
import jargyle.server.SettingSpec;
import jargyle.server.Settings;
import jargyle.server.SocksClientFactory;
import jargyle.server.TcpRelayServer;

public final class Socks5Worker implements Runnable {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = Logger.getLogger(
			Socks5Worker.class.getName());
	
	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private Socket clientSocket;
	private final Configuration configuration;
	private final Settings settings;
	private final SocksClient socksClient;
	
	public Socks5Worker(
			final Socket clientSock, 
			final Configuration config) {
		Settings sttngs = config.getSettings();
		SocksClient client = SocksClientFactory.newSocksClient(config);
		this.clientInputStream = null;
		this.clientOutputStream = null;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.settings = sttngs;
		this.socksClient = client;
	}
	
	private Socket authenticateUsing(final Method method) {
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format(String.format(
							"Unhandled method: %s", 
							method)),
					e);
			return null;
		}
		Socket socket = null;
		try {
			socket = authenticator.authenticate(
					this.clientSocket, this.configuration);
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in authenticating the client"), 
					e);
			return null;
		}
		return socket;
	}

	private boolean canAcceptExternalIncomingTcpAddress(
			final InetAddress externalIncomingTcpInetAddress) {
		Criteria allowedExternalIncomingTcpAddressCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ALLOWED_EXTERNAL_INCOMING_TCP_ADDRESS_CRITERIA, 
						Criteria.class);
		Criterion criterion = 
				allowedExternalIncomingTcpAddressCriteria.anyEvaluatesTrue(
						externalIncomingTcpInetAddress);
		if (criterion == null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"External incoming TCP address %s not allowed", 
							externalIncomingTcpInetAddress)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				return false;
			}
			return false;
		}
		Criteria blockedExternalIncomingTcpAddressCriteria =
				this.settings.getLastValue(
						SettingSpec.SOCKS5_BLOCKED_EXTERNAL_INCOMING_TCP_ADDRESS_CRITERIA, 
						Criteria.class);
		criterion = blockedExternalIncomingTcpAddressCriteria.anyEvaluatesTrue(
				externalIncomingTcpInetAddress);
		if (criterion != null) {
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"External incoming TCP address %s blocked based on the "
							+ "following criterion: %s", 
							externalIncomingTcpInetAddress,
							criterion)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean canAcceptSocks5Request(
			final InetAddress sourceInetAddress,
			final Socks5Request socks5Req) {
		Socks5RequestCriteria allowedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA, 
						Socks5RequestCriteria.class);
		Socks5RequestCriterion socks5RequestCriterion =
				allowedSocks5RequestCriteria.anyEvaluatesTrue(
						sourceInetAddress, socks5Req);
		if (socks5RequestCriterion == null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"SOCKS5 request from %s not allowed. "
							+ "SOCKS5 request: %s",
							sourceInetAddress.toString(),
							socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				return false;
			}
			return false;
		}
		Socks5RequestCriteria blockedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA, 
						Socks5RequestCriteria.class);
		socks5RequestCriterion =
				blockedSocks5RequestCriteria.anyEvaluatesTrue(
						sourceInetAddress, socks5Req);
		if (socks5RequestCriterion != null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"SOCKS5 request from %s blocked based on the "
							+ "following criterion: %s. SOCKS5 request: %s",
							sourceInetAddress.toString(),
							socks5RequestCriterion.toString(),
							socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureClientDatagramSocket(
			final DatagramSocket clientDatagramSock) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(clientDatagramSock);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureClientSocket(final Socket clientSocket) {
		try {
			SocketSettings socketSettings = 
					this.settings.getLastValue(
							SettingSpec.CLIENT_SOCKET_SETTINGS,
							SocketSettings.class);
			socketSettings.applyTo(clientSocket);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the client socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureIncomingSocket(final Socket incomingSocket) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_INCOMING_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(incomingSocket);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the incoming socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureListenSocket(final ServerSocket listenSocket) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(listenSocket);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the listen socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureServerDatagramSocket(
			final DatagramSocket serverDatagramSock) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverDatagramSock);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the server-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureServerSocket(final Socket serverSocket) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverSocket);
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_BIND_HOST, Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			serverSocket.bind(new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in binding the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private void doBind(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		ServerSocketFactory serverSocketFactory = 
				ServerSocketFactory.newInstance(this.socksClient);
		ServerSocket listenSocket = null;
		Socket incomingSocket = null;
		try {
			listenSocket = serverSocketFactory.newServerSocket();
			if (!this.configureListenSocket(listenSocket)) {
				return;
			}
			try {
				listenSocket.bind(new InetSocketAddress(
						InetAddress.getByName(desiredDestinationAddress),
						desiredDestinationPort));
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in binding the listen socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"Sending %s", 
								socks5Rep.toString())));
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			try {
				incomingSocket = listenSocket.accept();
				if (!this.configureIncomingSocket(incomingSocket)) {
					return;
				}
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in waiting for an incoming socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"Sending %s", 
								socks5Rep.toString())));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			} finally {
				listenSocket.close();
			}
			InetAddress incomingTcpInetAddress = incomingSocket.getInetAddress();
			if (!this.canAcceptExternalIncomingTcpAddress(incomingTcpInetAddress)) {
				return;
			}
			serverBoundAddress = incomingTcpInetAddress.getHostAddress();
			addressType = AddressType.get(serverBoundAddress);
			serverBoundPort = incomingSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					addressType, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			this.passData(
					incomingSocket, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (incomingSocket != null && !incomingSocket.isClosed()) {
				incomingSocket.close();
			}
			if (listenSocket != null && !listenSocket.isClosed()) {
				listenSocket.close();
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
		Socket serverSocket = null;
		try {
			serverSocket = socketFactory.newSocket();
			if (!this.configureServerSocket(serverSocket)) {
				return;
			}
			try {
				int connectTimeout = this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue();
				serverSocket.connect(new InetSocketAddress(
						InetAddress.getByName(desiredDestinationAddress),
						desiredDestinationPort),
						connectTimeout);
			} catch (UnknownHostException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"Sending %s", 
								socks5Rep.toString())));
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format("Sending %s", socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			this.passData(
					serverSocket, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(),
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		}
	}
	
	private void doUdpAssociate(
			final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		DatagramSocket serverDatagramSock = null;
		DatagramSocket clientDatagramSock = null;
		UdpRelayServer udpRelayServer = null;
		try { 
			serverDatagramSock = this.newServerDatagramSocket();
			if (serverDatagramSock == null) {
				return;
			}
			if (!this.configureServerDatagramSocket(serverDatagramSock)) {
				return;
			}
			clientDatagramSock = this.newClientDatagramSocket();
			if (clientDatagramSock == null) {
				return;
			}
			if (!this.configureClientDatagramSocket(clientDatagramSock)) {
				return;
			}
			InetAddress inetAddress = clientDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			if (!serverBoundAddress.matches("[a-zA-Z1-9]")) {
				inetAddress = this.clientSocket.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			AddressType addressType = AddressType.get(serverBoundAddress);
			int serverBoundPort = clientDatagramSock.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					addressType, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			udpRelayServer = new UdpRelayServer(
					clientDatagramSock,
					serverDatagramSock,
					this.clientSocket.getInetAddress().getHostAddress(),
					desiredDestinationAddress,
					desiredDestinationPort, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ALLOWED_EXTERNAL_INCOMING_UDP_ADDRESS_CRITERIA, 
							Criteria.class), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_BLOCKED_EXTERNAL_INCOMING_UDP_ADDRESS_CRITERIA, 
							Criteria.class), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in starting the UDP association"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.log(
						Level.FINE, 
						this.format(String.format(
								"Sending %s", 
								socks5Rep.toString())));
				this.writeThenFlush(socks5Rep.toByteArray());				
			} 
		} finally {
			if (udpRelayServer != null && !udpRelayServer.isStopped()) {
				udpRelayServer.stop();
			}
			if (clientDatagramSock != null && !clientDatagramSock.isClosed()) {
				clientDatagramSock.close();
			}
			if (serverDatagramSock != null && !serverDatagramSock.isClosed()) {
				serverDatagramSock.close();
			}
		}
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
	private DatagramSocket newClientDatagramSocket() {
		DatagramSocket clientDatagramSock = null;
		try {
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_BIND_HOST, 
					Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			DatagramPacketFilter datagramPacketFilter = 
					DatagramPacketFilterFactory.newDatagramPacketFilter(
							this.clientSocket);
			clientDatagramSock = new FilterDatagramSocket(
					datagramPacketFilter,
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in creating the client-facing UDP "
							+ "socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return null;
			}
			return null;
		}
		return clientDatagramSock;
	}
	
	private DatagramSocket newServerDatagramSocket() {
		DatagramSocket serverDatagramSock = null;
		try {
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_BIND_HOST, 
					Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			DatagramSocketFactory datagramSocketFactory = 
					DatagramSocketFactory.newInstance(this.socksClient);
			serverDatagramSock = datagramSocketFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in creating the server-facing UDP "
							+ "socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return null;
			}
			return null;
		}
		return serverDatagramSock;
	}
	
	private Socks5Request newSocks5Request() throws IOException {
		Socks5Request socks5Req = null;
		try {
			socks5Req = Socks5Request.newInstanceFrom(this.clientInputStream);
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in parsing the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			return null;
		}
		LOGGER.log(
				Level.FINE, 
				this.format(String.format(
						"Received %s", 
						socks5Req.toString())));
		return socks5Req;
	}
	
	private void passData(
			final Socket serverSocket, 
			final int bufferSize, 
			final int timeout) {
		TcpRelayServer tcpRelayServer = new TcpRelayServer(
				this.clientSocket, 
				serverSocket, 
				bufferSize, 
				timeout);
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
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in starting to pass data"), 
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
			this.clientInputStream = this.clientSocket.getInputStream();
			this.clientOutputStream = this.clientSocket.getOutputStream();
			Method method = this.selectMethod();
			if (method == null) { return; } 
			Socket socket = this.authenticateUsing(method);
			if (socket == null) { return; } 
			this.clientInputStream = socket.getInputStream();
			this.clientOutputStream = socket.getOutputStream();
			this.clientSocket = socket;
			if (!this.configureClientSocket(this.clientSocket)) {
				return;
			}
			Socks5Request socks5Req = this.newSocks5Request();
			if (socks5Req == null) { return; }
			if (!this.canAcceptSocks5Request(
					this.clientSocket.getInetAddress(), socks5Req)) {
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
			LOGGER.log(
					Level.WARNING, 
					this.format("Internal server error"), 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.log(
							Level.WARNING, 
							this.format("Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
		}
	}
	
	private Method selectMethod() throws IOException {
		InputStream in = new SequenceInputStream(new ByteArrayInputStream(
				new byte[] { Version.V5.byteValue() }),
				this.clientInputStream);
		ClientMethodSelectionMessage cmsm = null;
		try {
			cmsm = ClientMethodSelectionMessage.newInstanceFrom(in); 
		} catch (IllegalArgumentException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in parsing the method selection "
							+ "message from the client"), 
					e);
			return null;
		}
		LOGGER.log(
				Level.FINE, 
				this.format(String.format("Received %s", cmsm.toString())));
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
		LOGGER.log(
				Level.FINE, 
				this.format(String.format("Sending %s", smsm.toString())));
		this.writeThenFlush(smsm.toByteArray());
		return method;
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
