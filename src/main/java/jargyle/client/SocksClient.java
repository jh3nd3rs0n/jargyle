package jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import jargyle.common.net.SocketSettings;

public abstract class SocksClient {

	public static abstract class Builder {
		
		private String bindHost;
		private int bindPort;
		private int connectTimeout;
		private SocketSettings socketSettings;
		private final SocksServerUri socksServerUri;
		
		public Builder(final SocksServerUri serverUri) {
			if (serverUri == null) {
				throw new NullPointerException(
						"SOCKS server URI must not be null");
			}
			this.bindHost = DEFAULT_BIND_HOST;
			this.bindPort = DEFAULT_BIND_PORT;
			this.connectTimeout = DEFAULT_CONNECT_TIMEOUT;
			this.socketSettings = DEFAULT_SOCKET_SETTINGS;
			this.socksServerUri = serverUri;
		}
		
		public Builder bindHost(final String s) {
			if (s != null && s.isEmpty()) {
				throw new IllegalArgumentException(
						"bind host must not be empty");
			}
			this.bindHost = s;
			return this;
		}
		
		public Builder bindPort(final int i) {
			if (i < MIN_BIND_PORT || i > MAX_BIND_PORT) {
				throw new IllegalArgumentException(String.format(
						"bind port must be an integer between %s and %s "
						+ "(inclusive).", 
						MIN_BIND_PORT,
						MAX_BIND_PORT));
			}
			this.bindPort = i;
			return this;
		}
		
		public abstract SocksClient build();
		
		public Builder connectTimeout(final int i) {
			if (i < 0) {
				throw new IllegalArgumentException(
						"connect timeout must be at least 0");
			}
			this.connectTimeout = i;
			return this;
		}
		
		public Builder fromSystemProperties() {
			String bindHostProperty = System.getProperty(
					"socksClient.bindHost");
			if (bindHostProperty != null) {
				this.bindHost(bindHostProperty);
			}
			String connectTimeoutProperty = System.getProperty(
					"socksClient.connectTimeout");
			if (connectTimeoutProperty != null) {
				String message = String.format(
						"connect timeout must be an integer between "
						+ "%s and %s (inclusive)", 
						0,
						Integer.MAX_VALUE);
				int connectTmt = -1;
				try {
					connectTmt = Integer.parseInt(connectTimeoutProperty);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(message, e);
				}
				this.connectTimeout(connectTmt);
			}
			String socketSettingsProperty = System.getProperty(
					"socksClient.socketSettings");
			if (socketSettingsProperty != null) {
				SocketSettings socketSttngs = SocketSettings.newInstance(
						socketSettingsProperty);
				this.socketSettings(socketSttngs);
			}
			return this;
		}
		
		public Builder socketSettings(final SocketSettings s) {
			this.socketSettings = s;
			return this;
		}

	}
	
	public static final String DEFAULT_BIND_HOST = "0.0.0.0";
	public static final int DEFAULT_BIND_PORT = 0;
	public static final int DEFAULT_CONNECT_TIMEOUT = 60000; // 1 minute
	public static final SocketSettings DEFAULT_SOCKET_SETTINGS = 
			SocketSettings.newInstance();
	public static final int MAX_BIND_PORT = 0xffff;
	public static final int MIN_BIND_PORT = 0;

	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			socksClient = socksServerUri.newSocksClientBuilder()
					.fromSystemProperties()
					.build();
		}
		return socksClient;
	}
	
	private final String bindHost;
	private final int bindPort;
	private final int connectTimeout;
	private final SocketSettings socketSettings;
	private final SocksServerUri socksServerUri;
	
	protected SocksClient(final Builder builder) {
		String bindHst = builder.bindHost;
		int bindPrt = builder.bindPort;
		int connectTmt = builder.connectTimeout;
		SocketSettings socketSttngs = builder.socketSettings;
		if (bindHst == null) {
			bindHst = DEFAULT_BIND_HOST;
		}
		if (socketSttngs == null) {
			socketSttngs = DEFAULT_SOCKET_SETTINGS;
		}
		socketSttngs = SocketSettings.newInstance(socketSttngs);
		SocksServerUri serverUri = builder.socksServerUri;
		this.bindHost = bindHst;
		this.bindPort = bindPrt;
		this.connectTimeout = connectTmt;
		this.socketSettings = socketSttngs;
		this.socksServerUri = serverUri;
	}
	
	public final void bind(final Socket socket) throws IOException {
		socket.bind(new InetSocketAddress(
				InetAddress.getByName(this.bindHost), this.bindPort));
	}
	
	public final void connectToSocksServerWith(
			final Socket socket) throws IOException {
		this.connectToSocksServerWith(socket, this.connectTimeout);
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
	
	public final String getBindHost() {
		return this.bindHost;
	}
	
	public final int getBindPort() {
		return this.bindPort;
	}
	
	public final int getConnectTimeout() {
		return this.connectTimeout;
	}
	
	public final SocketSettings getSocketSettings() {
		return SocketSettings.newInstance(this.socketSettings);
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
