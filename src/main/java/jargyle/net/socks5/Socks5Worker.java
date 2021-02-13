package jargyle.net.socks5;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.net.DatagramSocketInterface;
import jargyle.net.DatagramSocketInterfaceFactory;
import jargyle.net.DirectDatagramSocketInterface;
import jargyle.net.Host;
import jargyle.net.HostnameResolver;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.ServerSocketInterface;
import jargyle.net.ServerSocketInterfaceFactory;
import jargyle.net.SocketInterface;
import jargyle.net.SocketInterfaceFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.Configuration;
import jargyle.net.socks.ExternalTrafficRouter;
import jargyle.net.socks.SettingSpec;
import jargyle.net.socks.Settings;
import jargyle.net.socks.ServerSideSslWrapper;
import jargyle.net.socks.TcpRelayServer;
import jargyle.net.socks5.gssapiauth.GssDatagramSocketInterface;
import jargyle.net.socks5.gssapiauth.GssSocketInterface;
import jargyle.util.Criteria;
import jargyle.util.Criterion;
import jargyle.util.PositiveInteger;

public final class Socks5Worker implements Runnable {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = Logger.getLogger(
			Socks5Worker.class.getName());

	private InputStream clientInputStream;
	private OutputStream clientOutputStream;
	private SocketInterface clientSocketInterface;
	private final Configuration configuration;
	private final ExternalTrafficRouter externalTrafficRouter;
	private final ServerSideSslWrapper serverSideSslWrapper;	
	private final Settings settings;
	
	public Socks5Worker(
			final SocketInterface clientSockInterface, 
			final Configuration config, 
			final ServerSideSslWrapper wrapper, 
			final ExternalTrafficRouter router) {
		Settings sttngs = config.getSettings();
		this.clientInputStream = null;
		this.clientOutputStream = null;
		this.clientSocketInterface = clientSockInterface;
		this.configuration = config;
		this.externalTrafficRouter = router;
		this.serverSideSslWrapper = wrapper;		
		this.settings = sttngs;
	}
	
	private SocketInterface authenticateUsing(final Method method) {
		ServerSideAuthenticator serverSideAuthenticator = null;
		try {
			serverSideAuthenticator = ServerSideAuthenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format(String.format(
							"Unhandled method: %s", 
							method)),
					e);
			return null;
		}
		SocketInterface socketInterface = null;
		try {
			socketInterface = serverSideAuthenticator.authenticate(
					this.clientSocketInterface, this.configuration);
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in authenticating the client"), 
					e);
			return null;
		}
		return socketInterface;
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"External incoming address %s not allowed", 
							externalIncomingAddress)));
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
				LOGGER.log(
						Level.WARNING, 
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"External incoming address %s blocked based on the "
							+ "following criterion: %s", 
							externalIncomingAddress,
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
				LOGGER.log(
						Level.WARNING, 
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"SOCKS5 request from %s not allowed. "
							+ "SOCKS5 request: %s",
							clientAddress,
							socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
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
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"SOCKS5 request from %s blocked based on the "
							+ "following criterion: %s. SOCKS5 request: %s",
							clientAddress,
							socks5RequestCriterion.toString(),
							socks5Req.toString())));
			try {
				this.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureClientDatagramSocketInterface(
			final DatagramSocketInterface clientDatagramSockInterface) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(clientDatagramSockInterface);
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
	
	private boolean configureClientSocketInterface(
			final SocketInterface clientSocketInterface) {
		try {
			SocketSettings socketSettings = 
					this.settings.getLastValue(
							SettingSpec.CLIENT_SOCKET_SETTINGS,
							SocketSettings.class);
			socketSettings.applyTo(clientSocketInterface);
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in setting the client socket"), 
						e1);				
			}
			return false;
		}
		return true;
	}
	
	private boolean configureExternalIncomingSocketInterface(
			final SocketInterface externalIncomingSocketInterface) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_EXTERNAL_INCOMING_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(externalIncomingSocketInterface);
		} catch (SocketException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in setting the external incoming socket"), 
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureListenSocketInterface(
			final ServerSocketInterface listenSocketInterface) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(listenSocketInterface);
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureServerDatagramSocketInterface(
			final DatagramSocketInterface serverDatagramSockInterface) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverDatagramSockInterface);
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureServerSocketInterface(
			final SocketInterface serverSocketInterface) {
		try {
			SocketSettings socketSettings = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS, 
					SocketSettings.class);
			socketSettings.applyTo(serverSocketInterface);
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_BIND_HOST, Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			serverSocketInterface.bind(new InetSocketAddress(bindInetAddress, 0));
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
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
				LOGGER.log(
						Level.WARNING, 
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
				this.externalTrafficRouter.newHostnameResolverFactory();
		ServerSocketInterfaceFactory serverSocketInterfaceFactory = 
				this.externalTrafficRouter.newServerSocketInterfaceFactory();
		HostnameResolver hostnameResolver = null;
		ServerSocketInterface listenSocketInterface = null;
		SocketInterface externalIncomingSocketInterface = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
			listenSocketInterface = 
					serverSocketInterfaceFactory.newServerSocketInterface();
			if (!this.configureListenSocketInterface(listenSocketInterface)) {
				return;
			}
			try {
				listenSocketInterface.bind(new InetSocketAddress(
						hostnameResolver.resolve(desiredDestinationAddress),
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
			InetAddress inetAddress = listenSocketInterface.getInetAddress();
			String serverBoundAddress =	inetAddress.getHostAddress();
			AddressType addressType = AddressType.get(serverBoundAddress);
			int serverBoundPort = listenSocketInterface.getLocalPort();
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
				externalIncomingSocketInterface = listenSocketInterface.accept();
				if (!this.configureExternalIncomingSocketInterface(
						externalIncomingSocketInterface)) {
					return;
				}
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in waiting for an external incoming socket"), 
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
				listenSocketInterface.close();
			}
			InetAddress externalIncomingInetAddress = 
					externalIncomingSocketInterface.getInetAddress();
			String externalIncomingAddress = 
					externalIncomingInetAddress.getHostAddress();
			if (!this.canAcceptExternalIncomingAddress(
					externalIncomingAddress)) {
				return;
			}
			serverBoundAddress = externalIncomingAddress;
			addressType = AddressType.get(serverBoundAddress);
			serverBoundPort = externalIncomingSocketInterface.getLocalPort();
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
					externalIncomingSocketInterface, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(), 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (externalIncomingSocketInterface != null 
					&& !externalIncomingSocketInterface.isClosed()) {
				externalIncomingSocketInterface.close();
			}
			if (listenSocketInterface != null 
					&& !listenSocketInterface.isClosed()) {
				listenSocketInterface.close();
			}
		}
	}
	
	private void doConnect(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		HostnameResolverFactory hostnameResolverFactory =
				this.externalTrafficRouter.newHostnameResolverFactory();
		SocketInterfaceFactory socketInterfaceFactory = 
				this.externalTrafficRouter.newSocketInterfaceFactory();
		HostnameResolver hostnameResolver = null;
		SocketInterface serverSocketInterface = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
			serverSocketInterface = socketInterfaceFactory.newSocketInterface();
			if (!this.configureServerSocketInterface(serverSocketInterface)) {
				return;
			}
			try {
				int connectTimeout = this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue();
				serverSocketInterface.connect(new InetSocketAddress(
						hostnameResolver.resolve(desiredDestinationAddress),
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
					serverSocketInterface.getInetAddress().getHostAddress();
			AddressType addressType = AddressType.get(serverBoundAddress);
			int serverBoundPort = serverSocketInterface.getPort();
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
					serverSocketInterface, 
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE, 
							PositiveInteger.class).intValue(),
					this.settings.getLastValue(
							SettingSpec.SOCKS5_ON_CONNECT_RELAY_TIMEOUT, 
							PositiveInteger.class).intValue());
		} finally {
			if (serverSocketInterface != null && !serverSocketInterface.isClosed()) {
				serverSocketInterface.close();
			}
		}
	}
	
	private void doResolve(final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		HostnameResolverFactory hostnameResolverFactory = 
				this.externalTrafficRouter.newHostnameResolverFactory();
		HostnameResolver hostnameResolver = 
				hostnameResolverFactory.newHostnameResolver();
		InetAddress inetAddress = null;
		try {
			inetAddress = hostnameResolver.resolve(desiredDestinationAddress);
		} catch (UnknownHostException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
			LOGGER.log(
					Level.FINE, 
					this.format(String.format(
							"Sending %s", 
							socks5Rep.toString())));
			this.writeThenFlush(socks5Rep.toByteArray());
			return;			
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in resolving the hostname"), 
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
		String serverBoundAddress = inetAddress.getHostAddress();
		AddressType addressType = AddressType.get(serverBoundAddress);
		int serverBoundPort = desiredDestinationPort;
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				addressType, 
				serverBoundAddress, 
				serverBoundPort);
		LOGGER.log(
				Level.FINE, 
				this.format(String.format("Sending %s", socks5Rep.toString())));
		this.writeThenFlush(socks5Rep.toByteArray());		
	}
	
	private void doUdpAssociate(
			final Socks5Request socks5Req) throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddress = 
				socks5Req.getDesiredDestinationAddress();
		if (!desiredDestinationAddress.matches("[a-zA-Z1-9]")) {
			desiredDestinationAddress = 
					this.clientSocketInterface.getInetAddress().getHostAddress();
		}
		int desiredDestinationPort = socks5Req.getDesiredDestinationPort();
		HostnameResolverFactory hostnameResolverFactory = 
				this.externalTrafficRouter.newHostnameResolverFactory();
		HostnameResolver hostnameResolver = null;
		DatagramSocketInterface serverDatagramSockInterface = null;
		DatagramSocketInterface clientDatagramSockInterface = null;
		try {
			hostnameResolver = hostnameResolverFactory.newHostnameResolver();
			serverDatagramSockInterface = this.newServerDatagramSocketInterface();
			if (serverDatagramSockInterface == null) {
				return;
			}
			if (!this.configureServerDatagramSocketInterface(
					serverDatagramSockInterface)) {
				return;
			}
			clientDatagramSockInterface = this.newClientDatagramSocketInterface();
			if (clientDatagramSockInterface == null) {
				return;
			}
			if (!this.configureClientDatagramSocketInterface(
					clientDatagramSockInterface)) {
				return;
			}
			DatagramSocketInterface clientDatagramSock = 
					this.wrapClientDatagramSocketInterface(
							clientDatagramSockInterface, 
							desiredDestinationAddress, 
							desiredDestinationPort); 
			if (clientDatagramSock == null) {
				return;
			}
			clientDatagramSockInterface = clientDatagramSock;
			InetAddress inetAddress = clientDatagramSockInterface.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			if (!serverBoundAddress.matches("[a-zA-Z1-9]")) {
				inetAddress = this.clientSocketInterface.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			AddressType addressType = AddressType.get(serverBoundAddress);
			int serverBoundPort = clientDatagramSockInterface.getLocalPort();
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
			this.passPackets(
					new UdpRelayServer.ClientSocketAddress(
							desiredDestinationAddress, desiredDestinationPort),
					new UdpRelayServer.DatagramSocketInterfaces(
							clientDatagramSockInterface, 
							serverDatagramSockInterface), 
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
			if (clientDatagramSockInterface != null 
					&& !clientDatagramSockInterface.isClosed()) {
				clientDatagramSockInterface.close();
			}
			if (serverDatagramSockInterface != null 
					&& !serverDatagramSockInterface.isClosed()) {
				serverDatagramSockInterface.close();
			}
		}
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
	private DatagramSocketInterface newClientDatagramSocketInterface() {
		DatagramSocketInterface clientDatagramSockInterface = null;
		try {
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_BIND_HOST, 
					Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			clientDatagramSockInterface = new DirectDatagramSocketInterface(
					new DatagramSocket(new InetSocketAddress(bindInetAddress, 0)));
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);
			}
			return null;
		}
		return clientDatagramSockInterface;
	}
	
	private DatagramSocketInterface newServerDatagramSocketInterface() {
		DatagramSocketInterface serverDatagramSock = null;
		try {
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_BIND_HOST, 
					Host.class);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			DatagramSocketInterfaceFactory datagramSocketInterfaceFactory = 
					this.externalTrafficRouter.newDatagramSocketInterfaceFactory();
			serverDatagramSock = 
					datagramSocketInterfaceFactory.newDatagramSocketInterface(
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
				LOGGER.log(
						Level.WARNING, 
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
			final SocketInterface serverSocketInterface, 
			final int bufferSize, 
			final int timeout) {
		TcpRelayServer tcpRelayServer = new TcpRelayServer(
				this.clientSocketInterface, 
				serverSocketInterface, 
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
	
	private void passPackets(
			final UdpRelayServer.ClientSocketAddress clientSocketAddress,
			final UdpRelayServer.DatagramSocketInterfaces datagramSocketInterfaces,
			final HostnameResolver hostnameResolver,
			final UdpRelayServer.ExternalIncomingAddressCriteria externalIncomingAddressCriteria,
			final UdpRelayServer.ExternalOutgoingAddressCriteria externalOutgoingAddressCriteria, 
			final UdpRelayServer.RelaySettings relaySettings) {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientSocketAddress,
				datagramSocketInterfaces,
				hostnameResolver,
				externalIncomingAddressCriteria, 
				externalOutgoingAddressCriteria,
				relaySettings);
		try {
			udpRelayServer.start();
			while (!this.clientSocketInterface.isClosed()
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
		} finally {
			if (!udpRelayServer.isStopped()) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() {
		try {
			this.clientInputStream = this.clientSocketInterface.getInputStream();
			this.clientOutputStream = this.clientSocketInterface.getOutputStream();
			Method method = this.selectMethod();
			if (method == null) { return; } 
			SocketInterface socketInterface = this.authenticateUsing(method);
			if (socketInterface == null) { return; } 
			this.clientInputStream = socketInterface.getInputStream();
			this.clientOutputStream = socketInterface.getOutputStream();
			this.clientSocketInterface = socketInterface;
			if (!this.configureClientSocketInterface(this.clientSocketInterface)) {
				return;
			}
			Socks5Request socks5Req = this.newSocks5Request();
			if (socks5Req == null) { return; }
			if (!this.canAcceptSocks5Request(
					this.clientSocketInterface.getInetAddress().getHostAddress(), 
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
			LOGGER.log(
					Level.WARNING, 
					this.format("Internal server error"), 
					t);
		} finally {
			if (!this.clientSocketInterface.isClosed()) {
				try {
					this.clientSocketInterface.close();
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
			.append(" [clientSocketInterface=")
			.append(this.clientSocketInterface)
			.append("]");
		return builder.toString();
	}
	
	private DatagramSocketInterface wrapClientDatagramSocketInterface(
			final DatagramSocketInterface clientDatagramSockInterface, 
			final String peerHost, 
			final int peerPort) {
		DatagramSocketInterface clientDatagramSock = null;
		try {
			clientDatagramSock = this.serverSideSslWrapper.wrapIfSslEnabled(
					clientDatagramSockInterface, peerHost, peerPort);
		} catch (IOException e) {
			LOGGER.log(
					Level.WARNING, 
					this.format("Error in wrapping the client-facing UDP "
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
				LOGGER.log(
						Level.WARNING, 
						this.format("Error in writing SOCKS5 reply"), 
						e1);				
			}
			return null;
		}
		if (this.clientSocketInterface instanceof GssSocketInterface) {
			GssSocketInterface gssSocketInterface = 
					(GssSocketInterface) this.clientSocketInterface;
			clientDatagramSock = new GssDatagramSocketInterface(
					clientDatagramSock,
					gssSocketInterface.getGSSContext(),
					gssSocketInterface.getMessageProp());
		}
		return clientDatagramSock;
	}
	
	private void writeThenFlush(final byte[] b) throws IOException {
		this.clientOutputStream.write(b);
		this.clientOutputStream.flush();
	}
	
}
