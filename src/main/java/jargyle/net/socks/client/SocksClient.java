package jargyle.net.socks.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import jargyle.net.Host;
import jargyle.net.NetFactory;
import jargyle.net.Port;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.util.PositiveInteger;

public abstract class SocksClient {

	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			socksClient = socksServerUri.newSocksClient(Properties.newInstance());
		}
		return socksClient;
	}

	private final Properties properties;
	private final SocksServerUri socksServerUri;
	private final SslSocketFactory sslSocketFactory;
		
	public SocksClient(final SocksServerUri serverUri, final Properties props) {
		Objects.requireNonNull(
				serverUri, "SOCKS server URI must not be null");
		Objects.requireNonNull(props, "Properties must not be null");
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslSocketFactory = props.getValue(
				PropertySpec.SSL_ENABLED, Boolean.class).booleanValue() ? 
						new SslSocketFactoryImpl(this) : null;
	}
	
	public Socket getConnectedSocket(
			final Socket socket) throws IOException {
		return this.getConnectedSocket(
				socket, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				false);
	}
	
	public Socket getConnectedSocket(
			final Socket socket, 
			final boolean bindBeforeConnect) throws IOException {
		return this.getConnectedSocket(
				socket, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				bindBeforeConnect);
	}
	
	public Socket getConnectedSocket(
			final Socket socket, 
			final int timeout) throws IOException {
		return this.getConnectedSocket(socket, timeout, false);
	}
	
	public Socket getConnectedSocket(
			final Socket socket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		if (bindBeforeConnect) {
			socket.bind(new InetSocketAddress(
					this.properties.getValue(
							PropertySpec.BIND_HOST, Host.class).toInetAddress(), 
					this.properties.getValue(
							PropertySpec.BIND_PORT, Port.class).intValue()));
		}
		SocksServerUri socksServerUri = this.getSocksServerUri();
		socket.connect(
				new InetSocketAddress(
						InetAddress.getByName(socksServerUri.getHost()), 
						socksServerUri.getPort()), 
				timeout);
		if (this.sslSocketFactory == null) {
			return socket;
		}
		return this.sslSocketFactory.newSocket(
				socket, 
				this.socksServerUri.getHost(), 
				this.socksServerUri.getPort(), 
				true);
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	public abstract NetFactory newNetFactory();
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socksServerUri=")
			.append(this.socksServerUri)
			.append("]");
		return builder.toString();
	}
	
}
