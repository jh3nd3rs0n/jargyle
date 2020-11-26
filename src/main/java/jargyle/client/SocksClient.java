package jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.util.PositiveInteger;

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
	
	protected SocksClient(final SocksServerUri serverUri, final Properties props) {
		Objects.requireNonNull(
				serverUri, "SOCKS server URI must not be null");
		Objects.requireNonNull(props, "Properties must not be null");
		this.socksServerUri = serverUri;
		this.properties = props;
	}
	
	public final void bind(final Socket socket) throws IOException {
		socket.bind(new InetSocketAddress(
				InetAddress.getByName(this.properties.getValue(
						PropertySpec.BIND_HOST, Host.class).toString()), 
				this.properties.getValue(
						PropertySpec.BIND_PORT, Port.class).intValue()));
	}
	
	public final void connectToSocksServerWith(
			final Socket socket) throws IOException {
		this.connectToSocksServerWith(socket, this.properties.getValue(
				PropertySpec.CONNECT_TIMEOUT, PositiveInteger.class).intValue());
	}
	
	public final void connectToSocksServerWith(
			final Socket socket, final int timeout) throws IOException {
		SocksServerUri socksServerUri = this.getSocksServerUri();
		InetAddress inetAddress = InetAddress.getByName(
				socksServerUri.getHost());
		socket.connect(new InetSocketAddress(
				inetAddress, socksServerUri.getPort()), 
				timeout);
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	public abstract DatagramSocketFactory newDatagramSocketFactory();
	
	public abstract ServerSocketFactory newServerSocketFactory();
	
	public abstract SocketFactory newSocketFactory();

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
