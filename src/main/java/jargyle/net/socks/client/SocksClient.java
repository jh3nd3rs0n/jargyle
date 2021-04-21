package jargyle.net.socks.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jargyle.net.DefaultNetObjectFactory;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;
import jargyle.net.ssl.SslSocketFactory;

public abstract class SocksClient {

	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			List<Property<? extends Object>> properties = 
					new ArrayList<Property<? extends Object>>();
			for (PropertySpec<Object> propertySpec : PropertySpec.values()) {
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
		NetObjectFactory internalNetObjFactory = (chainedClient == null) ?
				new DefaultNetObjectFactory() 
				: chainedClient.newSocksNetObjectFactory();
		SslSocketFactory sslSockFactory = props.getValue(
				PropertySpec.SSL_ENABLED).booleanValue() ? 
						new SslSocketFactoryImpl(this) : null;
		this.chainedSocksClient = chainedClient;
		this.internalHostResolver = internalNetObjFactory.newHostResolver();
		this.internalNetObjectFactory = internalNetObjFactory;
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslSocketFactory = sslSockFactory;		
	}
	
	public final void configureInternalSocket(
			final Socket internalSocket) throws SocketException {
		SocketSettings socketSettings = this.properties.getValue(
				PropertySpec.SOCKET_SETTINGS);
		socketSettings.applyTo(internalSocket);
	}
	
	public final SocksClient getChainedSocksClient() {
		return this.chainedSocksClient;
	}
	
	public Socket getConnectedInternalSocket(
			final Socket internalSocket) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						PropertySpec.CONNECT_TIMEOUT).intValue(), 
				false);
	}
	
	public Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final boolean bindBeforeConnect) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						PropertySpec.CONNECT_TIMEOUT).intValue(), 
				bindBeforeConnect);
	}
	
	public Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout) throws IOException {
		return this.getConnectedInternalSocket(internalSocket, timeout, false);
	}
	
	public Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		if (bindBeforeConnect) {
			internalSocket.bind(new InetSocketAddress(
					this.properties.getValue(
							PropertySpec.BIND_HOST).toInetAddress(), 
					this.properties.getValue(
							PropertySpec.BIND_PORT).intValue()));
		}
		InetAddress socksServerUriHostInetAddress = 
				this.internalResolve(this.socksServerUri.getHost());
		int socksServerUriPort = this.socksServerUri.getPort();
		internalSocket.connect(
				new InetSocketAddress(
						socksServerUriHostInetAddress, 
						socksServerUriPort), 
				timeout);
		if (this.sslSocketFactory == null) {
			return internalSocket;
		}
		return this.sslSocketFactory.newSocket(
				internalSocket, 
				socksServerUriHostInetAddress.getHostAddress(), 
				socksServerUriPort, 
				true);
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	public final InetAddress internalResolve(
			final String host) throws IOException {
		return this.internalHostResolver.resolve(host);
	}
	
	public Socket newConnectedInternalSocket() throws IOException {
		return this.newConnectedInternalSocket(
				this.properties.getValue(
						PropertySpec.BIND_HOST).toInetAddress(),
				this.properties.getValue(
						PropertySpec.BIND_PORT).intValue());
	}
	
	public Socket newConnectedInternalSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket internalSocket = this.internalNetObjectFactory.newSocket(
				socksServerUriHost, 
				socksServerUriPort, 
				localAddr, 
				localPort);
		if (this.sslSocketFactory == null) {
			return internalSocket;
		}
		return this.sslSocketFactory.newSocket(
				internalSocket, 
				socksServerUriHost, 
				socksServerUriPort, 
				true);		
	}
	
	public final Socket newInternalSocket() {
		return this.internalNetObjectFactory.newSocket();
	}
	
	public abstract SocksNetObjectFactory newSocksNetObjectFactory();
	
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
