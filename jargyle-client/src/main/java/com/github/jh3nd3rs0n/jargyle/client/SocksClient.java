package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

public abstract class SocksClient {

	public static final class ClientSocketConnectParams {
		
		private Integer connectTimeout;
		private boolean mustBindBeforeConnect;		
		private NetObjectFactory netObjectFactory;
		private SocketSettings socketSettings;
		
		public ClientSocketConnectParams() {
			this.connectTimeout = null;
			this.mustBindBeforeConnect = true;
			this.netObjectFactory = null;
			this.socketSettings = null;
		}
		
		public Integer getConnectTimeout() {
			return this.connectTimeout;
		}
		
		public boolean getMustBindBeforeConnect() {
			return this.mustBindBeforeConnect;
		}
		
		public NetObjectFactory getNetObjectFactory() {
			return this.netObjectFactory;
		}
		
		public SocketSettings getSocketSettings() {
			return this.socketSettings;
		}

		public void setConnectTimeout(final Integer timeout) {
			this.connectTimeout = timeout;
		}

		public void setMustBindBeforeConnect(final boolean b) {
			this.mustBindBeforeConnect = b;
		}

		public void setNetObjectFactory(final NetObjectFactory netObjFactory) {
			this.netObjectFactory = netObjFactory;
		}

		public void setSocketSettings(final SocketSettings socketSttngs) {
			this.socketSettings = socketSttngs;
		}
		
	}
	
	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			List<Property<? extends Object>> properties = 
					new ArrayList<Property<? extends Object>>();
			for (PropertySpec<Object> propertySpec 
					: SocksClientPropertySpecConstants.values()) {
				String property = System.getProperty(propertySpec.getName());
				if (property != null) {
					properties.add(propertySpec.newPropertyWithParsableValue(
							property));
				}
			}			
			socksClient = socksServerUri.newSocksClient(
					Properties.newInstance(properties));
		}
		return socksClient;
	}
	
	private final SocksClient chainedSocksClient;
	private final HostResolver hostResolver;
	private final NetObjectFactory netObjectFactory;	
	private final Properties properties;
	private final SocksServerUri socksServerUri;
	private final SslSocketFactory sslSocketFactory;
	
	public SocksClient(final SocksServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	public SocksClient(
			final SocksServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		Objects.requireNonNull(
				serverUri, "SOCKS server URI must not be null");
		Objects.requireNonNull(props, "Properties must not be null");
		NetObjectFactory netObjFactory = chainedClient == null ?
				NetObjectFactory.getInstance() 
				: chainedClient.newSocksNetObjectFactory();
		SslSocketFactory sslSockFactory = 
				SslSocketFactoryImpl.isSslEnabled(props) ? 
						new SslSocketFactoryImpl(this) : null;
		this.chainedSocksClient = chainedClient;
		this.hostResolver = netObjFactory.newHostResolver();
		this.netObjectFactory = netObjFactory;
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslSocketFactory = sslSockFactory;
	}

	protected void configureClientSocket(
			final Socket clientSocket) throws SocketException {
		SocketSettings socketSettings = this.properties.getValue(
				GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS);
		socketSettings.applyTo(clientSocket);
	}
	
	private Socket getBoundConnectedClientSocket(
			final Socket clientSocket, 
			final ClientSocketConnectParams params,
			final InetAddress socksServerUriHostInetAddress,
			final int socksServerUriPort,
			final int connectTimeout) throws IOException {
		InetAddress clientBindHostInetAddress =
				this.properties.getValue(
						GeneralPropertySpecConstants.CLIENT_BIND_HOST).toInetAddress();
		PortRanges clientBindPortRanges = this.properties.getValue(
				GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES);
		NetObjectFactory netObjFactory = params.getNetObjectFactory();
		if (netObjFactory == null) {
			netObjFactory = this.netObjectFactory;
		}
		SocketSettings socketSttngs = params.getSocketSettings();
		if (socketSttngs == null) {
			socketSttngs = this.properties.getValue(
					GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS);
		}
		Socket clientSock = clientSocket;
		boolean clientSocketBound = false;
		for (Iterator<PortRange> iterator = clientBindPortRanges.toList().iterator();
				!clientSocketBound && iterator.hasNext();) {
			PortRange clientBindPortRange = iterator.next();
			for (Iterator<Port> iter = clientBindPortRange.iterator();
					!clientSocketBound && iter.hasNext();) {
				Port clientBindPort = iter.next();
				try {
					clientSock.bind(new InetSocketAddress(
							clientBindHostInetAddress, 
							clientBindPort.intValue()));
				} catch (SocketException e) {
					clientSock.close();
					clientSock = netObjFactory.newSocket();
					socketSttngs.applyTo(clientSock);
					continue;
				}
				try {
					clientSock.connect(new InetSocketAddress(
							socksServerUriHostInetAddress,
							socksServerUriPort), 
							connectTimeout);
				} catch (IOException e) {
					if (ThrowableHelper.isOrHasInstanceOf(
							e, BindException.class)) {
						clientSock.close();
						clientSock = netObjFactory.newSocket();
						socketSttngs.applyTo(clientSock);
						continue;
					}
					throw e;
				}
				clientSocketBound = true;
			}
		}
		if (!clientSocketBound) {
			throw new BindException(String.format(
					"unable to bind to the following address and port "
					+ "(range(s)): %s %s",
					clientBindHostInetAddress,
					clientBindPortRanges));
		}
		return clientSock;
	}
	
	public final SocksClient getChainedSocksClient() {
		return this.chainedSocksClient;
	}
	
	protected Socket getConnectedClientSocket(
			final Socket clientSocket, 
			final ClientSocketConnectParams params) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket clientSock = clientSocket;
		Integer connectTimeout = params.getConnectTimeout();
		if (connectTimeout == null) {
			connectTimeout = Integer.valueOf(this.properties.getValue(
					GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT).intValue());
		}
		InetAddress socksServerUriHostInetAddress =	this.resolve(
				socksServerUriHost);
		if (params.getMustBindBeforeConnect()) {
			clientSock = this.getBoundConnectedClientSocket(
					clientSock, 
					params, 
					socksServerUriHostInetAddress, 
					socksServerUriPort, 
					connectTimeout);
		} else {
			clientSock.connect(
					new InetSocketAddress(
							socksServerUriHostInetAddress, 
							socksServerUriPort), 
					connectTimeout);
		}
		if (this.sslSocketFactory == null) {
			return clientSock;
		}
		clientSock = this.sslSocketFactory.newSocket(
				clientSock, 
				socksServerUriHost, 
				socksServerUriPort, 
				true);
		return clientSock;		
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	protected Socket newClientSocket() {
		return this.netObjectFactory.newSocket();
	}
	
	protected Socket newConnectedClientSocket() throws IOException {
		InetAddress clientBindHostInetAddress = this.properties.getValue(
				GeneralPropertySpecConstants.CLIENT_BIND_HOST).toInetAddress();
		PortRanges clientBindPortRanges = this.properties.getValue(
				GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES);
		Socket clientSocket = null;
		boolean clientSocketBound = false;
		for (Iterator<PortRange> iterator = clientBindPortRanges.toList().iterator();
				!clientSocketBound && iterator.hasNext();) {
			PortRange clientBindPortRange = iterator.next();
			for (Iterator<Port> iter = clientBindPortRange.iterator();
					!clientSocketBound && iter.hasNext();) {
				Port clientBindPort = iter.next();
				try {
					clientSocket = this.newConnectedClientSocket(
							clientBindHostInetAddress, 
							clientBindPort.intValue());
				} catch (IOException e) {
					if (ThrowableHelper.isOrHasInstanceOf(
							e, BindException.class)) {
						continue;
					}
					throw e;
				}
				clientSocketBound = true;
			}
		}
		if (!clientSocketBound) {
			throw new BindException(String.format(
					"unable to bind to the following address and port "
					+ "(range(s)): %s %s",
					clientBindHostInetAddress,
					clientBindPortRanges));			
		}
		return clientSocket;
	}
	
	protected Socket newConnectedClientSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket clientSocket = this.netObjectFactory.newSocket(
				socksServerUriHost, 
				socksServerUriPort, 
				localAddr, 
				localPort);
		if (this.sslSocketFactory == null) {
			return clientSocket;
		}
		clientSocket = this.sslSocketFactory.newSocket(
				clientSocket, 
				socksServerUriHost, 
				socksServerUriPort, 
				true);
		return clientSocket;
	}
	
	public abstract SocksNetObjectFactory newSocksNetObjectFactory();
	
	protected InetAddress resolve(final String host) throws IOException {
		return this.hostResolver.resolve(host);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [chainedSocksClient=")
			.append(this.chainedSocksClient)		
			.append(", socksServerUri=")
			.append(this.socksServerUri)
			.append("]");
		return builder.toString();
	}
	
}
