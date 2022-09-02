package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientExceptionThrowingSocket;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

public abstract class SocksClient {

	public static final class InternalSocketConnectParams {
		
		private Integer connectTimeout;
		private boolean mustBindBeforeConnect;		
		private NetObjectFactory netObjectFactory;
		private SocketSettings socketSettings;
		
		public InternalSocketConnectParams() {
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
					: PropertySpecConstants.values()) {
				String property = System.getProperty(propertySpec.toString());
				if (property != null) {
					properties.add(propertySpec.newPropertyOfParsableValue(
							property));
				}
			}			
			socksClient = socksServerUri.newSocksClient(
					Properties.newInstance(properties));
		}
		return socksClient;
	}
	
	private final SocksClient chainedSocksClient;
	private final HostResolver internalHostResolver;
	private final NetObjectFactory internalNetObjectFactory;
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
		NetObjectFactory internalNetObjFactory = chainedClient == null ?
				NetObjectFactory.getInstance() 
				: chainedClient.newSocksNetObjectFactory();
		SslSocketFactory sslSockFactory = 
				SslSocketFactoryImpl.isSslEnabled(props) ? 
						new SslSocketFactoryImpl(this) : null;
		this.chainedSocksClient = chainedClient;
		this.internalHostResolver = internalNetObjFactory.newHostResolver();
		this.internalNetObjectFactory = internalNetObjFactory;
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslSocketFactory = sslSockFactory;
	}

	protected void configureInternalSocket(
			final Socket internalSocket) throws SocketException {
		SocketSettings socketSettings = this.properties.getValue(
				GeneralPropertySpecConstants.INTERNAL_SOCKET_SETTINGS);
		socketSettings.applyTo(internalSocket);
	}
	
	public final SocksClient getChainedSocksClient() {
		return this.chainedSocksClient;
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final InternalSocketConnectParams params) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket internalSock = internalSocket;
		Integer connectTimeout = params.getConnectTimeout();
		if (connectTimeout == null) {
			connectTimeout = Integer.valueOf(this.properties.getValue(
					GeneralPropertySpecConstants.INTERNAL_CONNECT_TIMEOUT).intValue());
		}
		try {
			InetAddress socksServerUriHostInetAddress =	this.resolve(
					socksServerUriHost);
			if (params.getMustBindBeforeConnect()) {
				InetAddress internalBindHostInetAddress = 
						this.properties.getValue(
								GeneralPropertySpecConstants.INTERNAL_BIND_HOST).toInetAddress();
				PortRanges internalBindPortRanges =	this.properties.getValue(
						GeneralPropertySpecConstants.INTERNAL_BIND_PORT_RANGES);
				NetObjectFactory netObjectFactory = params.getNetObjectFactory();
				if (netObjectFactory == null) {
					netObjectFactory = this.internalNetObjectFactory;
				}
				SocketSettings socketSettings = params.getSocketSettings();
				if (socketSettings == null) {
					socketSettings = this.properties.getValue(
							GeneralPropertySpecConstants.INTERNAL_SOCKET_SETTINGS);
				}
				boolean internalSocketBound = false;
				for (PortRange internalBindPortRange : internalBindPortRanges.toList()) {
					for (Port internalBindPort : internalBindPortRange) {
						try {
							internalSock.bind(new InetSocketAddress(
									internalBindHostInetAddress, 
									internalBindPort.intValue()));
						} catch (SocketException e) {
							internalSock.close();
							internalSock = netObjectFactory.newSocket();
							socketSettings.applyTo(internalSock);
							continue;
						}
						try {
							internalSock.connect(new InetSocketAddress(
									socksServerUriHostInetAddress,
									socksServerUriPort), 
									connectTimeout);
						} catch (IOException e) {
							if (e instanceof BindException 
									|| ThrowableHelper.getRecentCause(
											e, BindException.class) != null) {
								internalSock.close();
								internalSock = netObjectFactory.newSocket();
								socketSettings.applyTo(internalSock);
								continue;
							}
							throw e;
						}
						internalSocketBound = true;
						break;
					}
					if (internalSocketBound) {
						break;
					}
				}
				if (!internalSocketBound) {
					throw new BindException(String.format(
							"unable to bind to the following address and port "
							+ "ranges: %s %s",
							internalBindHostInetAddress,
							internalBindPortRanges));
				}
			} else {
				internalSock.connect(
						new InetSocketAddress(
								socksServerUriHostInetAddress, 
								socksServerUriPort), 
						connectTimeout);
			}
			if (this.sslSocketFactory == null) {
				return new SocksClientExceptionThrowingSocket(
						this, internalSock);
			}
			internalSock = this.sslSocketFactory.newSocket(
					internalSock, 
					socksServerUriHost, 
					socksServerUriPort, 
					true);
			internalSock = new SocksClientExceptionThrowingSocket(
					this, internalSock);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
		return internalSock;		
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	protected Socket newConnectedInternalSocket() throws IOException {
		InetAddress internalBindHostInetAddress = this.properties.getValue(
				GeneralPropertySpecConstants.INTERNAL_BIND_HOST).toInetAddress();
		PortRanges internalBindPortRanges = this.properties.getValue(
				GeneralPropertySpecConstants.INTERNAL_BIND_PORT_RANGES);
		for (PortRange internalBindPortRange : internalBindPortRanges.toList()) {
			for (Port internalBindPort : internalBindPortRange) {
				Socket internalSocket = null;
				try {
					internalSocket = this.newConnectedInternalSocket(
							internalBindHostInetAddress, 
							internalBindPort.intValue());
				} catch (IOException e) {
					if (e instanceof BindException 
							|| ThrowableHelper.getRecentCause(
									e, BindException.class) != null) {
						continue;
					}
					throw e;
				}
				return internalSocket;
			}
		}
		throw new BindException(String.format(
				"unable to bind to the following address and port ranges: %s %s",
				internalBindHostInetAddress,
				internalBindPortRanges));
	}
	
	protected Socket newConnectedInternalSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket internalSocket = null;
		try {
			internalSocket = this.internalNetObjectFactory.newSocket(
					socksServerUriHost, 
					socksServerUriPort, 
					localAddr, 
					localPort);
			if (this.sslSocketFactory == null) {
				return new SocksClientExceptionThrowingSocket(
						this, internalSocket);
			}
			internalSocket = this.sslSocketFactory.newSocket(
					internalSocket, 
					socksServerUriHost, 
					socksServerUriPort, 
					true);
			internalSocket = new SocksClientExceptionThrowingSocket(
					this, internalSocket);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
		return internalSocket;
	}
	
	protected Socket newInternalSocket() {
		return this.internalNetObjectFactory.newSocket();
	}
	
	public abstract SocksNetObjectFactory newSocksNetObjectFactory();
	
	protected InetAddress resolve(final String host) throws IOException {
		return this.internalHostResolver.resolve(host);
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
