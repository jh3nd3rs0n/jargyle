package jargyle.net.socks.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jargyle.internal.net.ssl.SslSocketFactory;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;

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

	private final Optional<SocksClient> chainedSocksClient;
	private final HostResolver internalHostResolver;
	private final NetObjectFactory internalNetObjectFactory;
	private final Properties properties;
	private final SocksServerUri socksServerUri;
	private final Optional<SslSocketFactory> sslSocketFactory;
		
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
		Optional<SocksClient> client = Optional.ofNullable(chainedClient);
		NetObjectFactory internalNetObjFactory = (!client.isPresent()) ?
				NetObjectFactory.newInstance() 
				: chainedClient.newSocksNetObjectFactory();
		Optional<SslSocketFactory> sslSockFactory = Optional.ofNullable( 
				(props.getValue(PropertySpec.SSL_ENABLED).booleanValue()) ?
						new SslSocketFactoryImpl(this) : null);
		this.chainedSocksClient = client;
		this.internalHostResolver = internalNetObjFactory.newHostResolver();
		this.internalNetObjectFactory = internalNetObjFactory;
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslSocketFactory = sslSockFactory;
	}
	
	protected void configureInternalSocket(
			final Socket internalSocket) throws SocketException {
		SocketSettings socketSettings = this.properties.getValue(
				PropertySpec.SOCKET_SETTINGS);
		socketSettings.applyTo(internalSocket);
	}
	
	public final Optional<SocksClient> getChainedSocksClient() {
		return this.chainedSocksClient;
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						PropertySpec.CONNECT_TIMEOUT).intValue(), 
				false);
	}
	
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final boolean bindBeforeConnect) throws IOException {
		return this.getConnectedInternalSocket(
				internalSocket, 
				this.properties.getValue(
						PropertySpec.CONNECT_TIMEOUT).intValue(), 
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
		if (bindBeforeConnect) {
			internalSocket.bind(new InetSocketAddress(
					this.properties.getValue(
							PropertySpec.BIND_HOST).toInetAddress(), 
					this.properties.getValue(
							PropertySpec.BIND_PORT).intValue()));
		}
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		InetAddress socksServerUriHostInetAddress =	this.resolve(
				socksServerUriHost);
		internalSocket.connect(
				new InetSocketAddress(
						socksServerUriHostInetAddress, socksServerUriPort), 
				timeout);
		if (!this.sslSocketFactory.isPresent()) {
			return internalSocket;
		}
		return this.sslSocketFactory.get().newSocket(
				internalSocket, 
				socksServerUriHost, 
				socksServerUriPort, 
				true);
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
						PropertySpec.BIND_HOST).toInetAddress(),
				this.properties.getValue(
						PropertySpec.BIND_PORT).intValue());
	}
	
	protected Socket newConnectedInternalSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		String socksServerUriHost = this.socksServerUri.getHost();
		int socksServerUriPort = this.socksServerUri.getPort();
		Socket internalSocket = this.internalNetObjectFactory.newSocket(
				socksServerUriHost, 
				socksServerUriPort, 
				localAddr, 
				localPort);
		if (!this.sslSocketFactory.isPresent()) {
			return internalSocket;
		}
		return this.sslSocketFactory.get().newSocket(
				internalSocket, 
				socksServerUriHost, 
				socksServerUriPort, 
				true);		
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
