package jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import jargyle.common.net.DatagramSocketFactory;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.ServerSocketFactory;
import jargyle.common.net.SocketFactory;
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
	
	public Socket connectToSocksServerWith(
			final Socket socket) throws IOException {
		return this.connectToSocksServerWith(
				socket, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				false);
	}
	
	public Socket connectToSocksServerWith(
			final Socket socket, 
			final boolean bindBeforeConnect) throws IOException {
		return this.connectToSocksServerWith(
				socket, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				bindBeforeConnect);
	}
	
	public Socket connectToSocksServerWith(
			final Socket socket, final int timeout) throws IOException {
		return this.connectToSocksServerWith(socket, timeout, false);
	}
	
	public Socket connectToSocksServerWith(
			final Socket socket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		if (bindBeforeConnect) {
			socket.bind(new InetSocketAddress(
					InetAddress.getByName(this.properties.getValue(
							PropertySpec.BIND_HOST, Host.class).toString()), 
					this.properties.getValue(
							PropertySpec.BIND_PORT, Port.class).intValue()));
		}
		SocksServerUri socksServerUri = this.getSocksServerUri();
		socket.connect(
				new InetSocketAddress(
						InetAddress.getByName(socksServerUri.getHost()), 
						socksServerUri.getPort()), 
				timeout);
		return socket;
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
