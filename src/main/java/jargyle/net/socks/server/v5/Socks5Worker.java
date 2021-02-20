package jargyle.net.socks.server.v5;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.DatagramSocketFactory;
import jargyle.net.Host;
import jargyle.net.HostnameResolver;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.NetFactory;
import jargyle.net.ServerSocketFactory;
import jargyle.net.SocketFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.server.SslWrapper;
import jargyle.net.socks.server.TcpRelayServer;
import jargyle.net.socks.transport.v5.AddressType;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.net.socks.transport.v5.Socks5Request;
import jargyle.net.socks.transport.v5.Version;
import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;
import jargyle.util.Criteria;
import jargyle.util.Criterion;
import jargyle.util.PositiveInteger;

public final class Socks5Worker implements Runnable {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Socks5Worker.class);

	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private Socket clientSocket;
	private final Configuration configuration;
	private final NetFactory externalNetFactory;
	private final Settings settings;
	private final SslWrapper sslWrapper;
	
	public Socks5Worker(
			final Socket clientSock, 
			final Configuration config, 
			final SslWrapper wrapper, 
			final NetFactory factory) {
		Settings sttngs = config.getSettings();
		this.clientInputStream = null;
		this.clientOutputStream = null;
		this.clientSocket = clientSock;
		this.configuration = config;
		this.externalNetFactory = factory;
		this.settings = sttngs;
		this.sslWrapper = wrapper;
	}
	
	private Socket authenticateUsing(final Method method) {
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			LOGGER.warn( 
					this.format(String.format(
							"Unhandled method: %s", 
							method)),
					e);
			return null;
		}
		Socket Socket = null;
		try {
			Socket = authenticator.authenticate(
					this.clientSocket, this.configuration);
		} catch (IOException e) {
			LOGGER.warn( 
					this.format("Error in authenticating the client"), 
					e);
			return null;
		}
		return Socket;
	}

	private boolean canAcceptExternalIncomingAddress(
			final String externalIncomingAddress) {
		Criteria allowedExternalIncomingAddressCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_BIND_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA, 
						Criteria.class);
		Criterion criterion = 
				allowedExternalIncomingAddressCriteria.anyEvaluatesTrue(
						externalIncomingAddress);
		if (criterion == null) {
			LOGGER.debug(this.format(String.format(
					"External incoming address %s not allowed",
					externalIncomingAddress)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		Criteria blockedExternalIncomingAddressCriteria =
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_BIND_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA, 
						Criteria.class);
		criterion = blockedExternalIncomingAddressCriteria.anyEvaluatesTrue(
				externalIncomingAddress);
		if (criterion != null) {
			LOGGER.debug(this.format(String.format(
					"External incoming address %s blocked based on the "
					+ "following criterion: %s",
					externalIncomingAddress,
					criterion)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private boolean canAcceptSocks5Request(
			final String clientAddress,
			final Socks5Request socks5Req) {
		Socks5RequestCriteria allowedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA, 
						Socks5RequestCriteria.class);
		Socks5RequestCriterion socks5RequestCriterion =
				allowedSocks5RequestCriteria.anyEvaluatesTrue(
						clientAddress, socks5Req);
		if (socks5RequestCriterion == null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(this.format(String.format(
					"SOCKS5 request from %s not allowed. SOCKS5 request: %s",
					clientAddress,
					socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn(
						this.format("Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		Socks5RequestCriteria blockedSocks5RequestCriteria = 
				this.settings.getLastValue(
						SettingSpec.SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA, 
						Socks5RequestCriteria.class);
		socks5RequestCriterion =
				blockedSocks5RequestCriteria.anyEvaluatesTrue(
						clientAddress, socks5Req);
		if (socks5RequestCriterion != null) {
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(this.format(String.format(
					"SOCKS5 request from %s blocked based on the following "
					+ "criterion: %s. SOCKS5 request: %s",
					clientAddress,
					socks5RequestCriterion.toString(),
					socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e);
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
			LOGGER.warn( 
					this.format("Error in setting the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
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
	
	private boolean configureExternalIncomingSocket(
			final Socket externalIncomingSocket) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_EXTERNAL_INCOMING_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(externalIncomingSocket);
		} catch (SocketException e) {
			LOGGER.warn( 
					this.format("Error in setting the external incoming socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
			LOGGER.warn( 
					this.format("Error in setting the listen socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
			LOGGER.warn( 
					this.format("Error in setting the server-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
			LOGGER.warn( 
					this.format("Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		} catch (IOException e) {
			LOGGER.warn( 
					this.format("Error in binding the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
		HostnameResolverFactory hostnameResolverFactory =
				this.externalNetFactory.newHostnameResolverFactory();
		ServerSocketFactory serverSocketFactory = 
				this.externalNetFactory.newServerSocketFactory();
		HostnameResolver hostnameResolver = null;
		ServerSocket listenSocket = null;
		Socket externalIncomingSocket = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
			listenSocket = serverSocketFactory.newServerSocket();
			if (!this.configureListenSocket(listenSocket)) {
				return;
			}
			try {
				listenSocket.bind(new InetSocketAddress(
						hostnameResolver.resolve(desiredDestinationAddress),
						desiredDestinationPort));
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in binding the listen socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.debug(this.format(String.format(
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
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			try {
				externalIncomingSocket = listenSocket.accept();
				if (!this.configureExternalIncomingSocket(
						externalIncomingSocket)) {
					return;
				}
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in waiting for an external incoming socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.debug(this.format(String.format(
						"Sending %s",
						socks5Rep.toString())));
				this.writeThenFlush(socks5Rep.toByteArray());
				return;
			} finally {
				listenSocket.close();
			}
			InetAddress externalIncomingInetAddress = 
					externalIncomingSocket.getInetAddress();
			String externalIncomingAddress = 
					externalIncomingInetAddress.getHostAddress();
			if (!this.canAcceptExternalIncomingAddress(
					externalIncomingAddress)) {
				return;
			}
			serverBoundAddress = externalIncomingAddress;
			addressType = AddressType.get(serverBoundAddress);
			serverBoundPort = externalIncomingSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					addressType, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			this.passData(
					externalIncomingSocket, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (externalIncomingSocket != null 
					&& !externalIncomingSocket.isClosed()) {
				externalIncomingSocket.close();
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
		HostnameResolverFactory hostnameResolverFactory =
				this.externalNetFactory.newHostnameResolverFactory();
		SocketFactory SocketFactory = 
				this.externalNetFactory.newSocketFactory();
		HostnameResolver hostnameResolver = null;
		Socket serverSocket = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
			serverSocket = SocketFactory.newSocket();
			if (!this.configureServerSocket(serverSocket)) {
				return;
			}
			try {
				int connectTimeout = this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue();
				serverSocket.connect(new InetSocketAddress(
						hostnameResolver.resolve(desiredDestinationAddress),
						desiredDestinationPort),
						connectTimeout);
			} catch (UnknownHostException e) {
				LOGGER.warn( 
						this.format("Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
				LOGGER.debug(this.format(String.format(
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
			LOGGER.debug(this.format(String.format(
					"Sending %s", 
					socks5Rep.toString())));
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
	
	private void doResolve(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		HostnameResolverFactory hostnameResolverFactory = 
				this.externalNetFactory.newHostnameResolverFactory();
		HostnameResolver hostnameResolver = 
				hostnameResolverFactory.newHostnameResolver();
		InetAddress inetAddress = null;
		try {
			inetAddress = hostnameResolver.resolve(desiredDestinationAddress);
		} catch (UnknownHostException e) {
			LOGGER.warn( 
					this.format("Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;			
		} catch (IOException e) {
			LOGGER.warn( 
					this.format("Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;
		}
		String serverBoundAddress = inetAddress.getHostAddress();
		AddressType addressType = AddressType.get(serverBoundAddress);
		int serverBoundPort = desiredDestinationPort;
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		LOGGER.debug(this.format(String.format(
				"Sending %s", 
				socks5Rep.toString())));
		this.writeThenFlush(socks5Rep.toByteArray());		
	}
	
	private void doUdpAssociate(
			final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		if (!desiredDestinationAddress.matches("[a-zA-Z1-9]")) {
			desiredDestinationAddress = 
					this.clientSocket.getInetAddress().getHostAddress();
		}
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		HostnameResolverFactory hostnameResolverFactory = 
				this.externalNetFactory.newHostnameResolverFactory();
		HostnameResolver hostnameResolver = null;
		DatagramSocket serverDatagramSock = null;
		DatagramSocket clientDatagramSock = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
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
			DatagramSocket clientDatagramSck = 
					this.wrapClientDatagramSocket(
							clientDatagramSock, 
							desiredDestinationAddress, 
							desiredDestinationPort); 
			if (clientDatagramSck == null) {
				return;
			}
			clientDatagramSock = clientDatagramSck;
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
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			this.passPackets(
					new UdpRelayServer.ClientSocketAddress(
							desiredDestinationAddress, desiredDestinationPort),
					new UdpRelayServer.DatagramSockets(
							clientDatagramSock, 
							serverDatagramSock), 
					hostnameResolver, 
					new UdpRelayServer.ExternalIncomingAddressCriteria(
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA, 
									Criteria.class), 
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA, 
									Criteria.class)), 
					new UdpRelayServer.ExternalOutgoingAddressCriteria(
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_OUTGOING_ADDRESS_CRITERIA, 
									Criteria.class), 
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_OUTGOING_ADDRESS_CRITERIA, 
									Criteria.class)), 
					new UdpRelayServer.RelaySettings(
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE, 
									PositiveInteger.class).intValue(), 
							this.settings.getLastValue(
									SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT, 
									PositiveInteger.class).intValue()));
		} finally {
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
			clientDatagramSock = new DatagramSocket(new InetSocketAddress(
					bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.warn( 
					this.format("Error in creating the client-facing UDP "
							+ "socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
					this.externalNetFactory.newDatagramSocketFactory();
			serverDatagramSock = 
					datagramSocketFactory.newDatagramSocket(
							new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.warn( 
					this.format("Error in creating the server-facing UDP "
							+ "socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
			LOGGER.warn( 
					this.format("Error in parsing the SOCKS5 request"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			return null;
		}
		LOGGER.debug(this.format(String.format(
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
			LOGGER.warn( 
					this.format("Error in starting to pass data"), 
					e);
		} finally {
			if (!tcpRelayServer.isStopped()) {
				tcpRelayServer.stop();
			}
		}
	}
	
	private void passPackets(
			final UdpRelayServer.ClientSocketAddress clientSocketAddress,
			final UdpRelayServer.DatagramSockets datagramSockets,
			final HostnameResolver hostnameResolver,
			final UdpRelayServer.ExternalIncomingAddressCriteria externalIncomingAddressCriteria,
			final UdpRelayServer.ExternalOutgoingAddressCriteria externalOutgoingAddressCriteria, 
			final UdpRelayServer.RelaySettings relaySettings) {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientSocketAddress,
				datagramSockets,
				hostnameResolver,
				externalIncomingAddressCriteria, 
				externalOutgoingAddressCriteria,
				relaySettings);
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
			LOGGER.warn( 
					this.format("Error in starting the UDP association"), 
					e);
		} finally {
			if (!udpRelayServer.isStopped()) {
				udpRelayServer.stop();
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
			Socks5Request socks5Req = this.newSocks5Request();
			if (socks5Req == null) { return; }
			if (!this.canAcceptSocks5Request(
					this.clientSocket.getInetAddress().getHostAddress(), 
					socks5Req)) {
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
			case RESOLVE:
				this.doResolve(socks5Req);
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
			LOGGER.warn( 
					this.format("Internal server error"), 
					t);
		} finally {
			if (!this.clientSocket.isClosed()) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					LOGGER.warn( 
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
			LOGGER.warn( 
					this.format("Error in parsing the method selection "
							+ "message from the client"), 
					e);
			return null;
		}
		LOGGER.debug(this.format(String.format(
				"Received %s", 
				cmsm.toString())));
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
		LOGGER.debug(this.format(String.format(
				"Sending %s", 
				smsm.toString())));
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
	
	private DatagramSocket wrapClientDatagramSocket(
			final DatagramSocket clientDatagramSock, 
			final String peerHost, 
			final int peerPort) {
		DatagramSocket clientDatagramSck = null;
		try {
			clientDatagramSck = this.sslWrapper.wrapIfSslEnabled(
					clientDatagramSock, peerHost, peerPort);
		} catch (IOException e) {
			LOGGER.warn( 
					this.format("Error in wrapping the client-facing UDP "
							+ "socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(this.format(String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						this.format("Error in writing SOCKS5 reply"), 
						e1);				
			}
			return null;
		}
		if (this.clientSocket instanceof GssSocket) {
			GssSocket gssSocket = (GssSocket) this.clientSocket;
			try {
				clientDatagramSck = new GssDatagramSocket(
						clientDatagramSck,
						gssSocket.getGSSContext(),
						gssSocket.getMessageProp());
			} catch (SocketException e) {
				LOGGER.warn( 
						this.format("Error creating GssDatagramSocket"), 
						e);
				return null;
			}
		}
		return clientDatagramSck;
	}
	
	private void writeThenFlush(final byte[] b) throws IOException {
		this.clientOutputStream.write(b);
		this.clientOutputStream.flush();
	}
	
}
