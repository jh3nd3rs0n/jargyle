package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.internal.net.SocksClientExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.client.internal.net.SocksClientExceptionThrowingSocket;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

public abstract class SocksClient {

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
				NetObjectFactory.newInstance() 
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
			final Socket internalSocket) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						GeneralPropertySpecConstants.INTERNAL_CONNECT_TIMEOUT).intValue(), 
				false);
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final boolean bindBeforeConnect) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						GeneralPropertySpecConstants.INTERNAL_CONNECT_TIMEOUT).intValue(), 
				bindBeforeConnect);
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout) throws IOException {
		return this.getConnectedInternalSocket(internalSocket, timeout, false);
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket internalSock = internalSocket;
		try {
			if (bindBeforeConnect) {
				internalSock.bind(new InetSocketAddress(
						this.properties.getValue(
								GeneralPropertySpecConstants.INTERNAL_BIND_HOST).toInetAddress(), 
						this.properties.getValue(
								GeneralPropertySpecConstants.INTERNAL_BIND_PORT).intValue()));
			}
			InetAddress socksServerUriHostInetAddress =	this.resolve(
					socksServerUriHost);
			internalSock.connect(
					new InetSocketAddress(
							socksServerUriHostInetAddress, socksServerUriPort), 
					timeout);
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
		return this.newConnectedInternalSocket(
				this.properties.getValue(
						GeneralPropertySpecConstants.INTERNAL_BIND_HOST).toInetAddress(),
				this.properties.getValue(
						GeneralPropertySpecConstants.INTERNAL_BIND_PORT).intValue());
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
