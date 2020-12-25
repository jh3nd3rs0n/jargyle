package jargyle.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import jargyle.common.net.DatagramSocketFactory;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.ServerSocketFactory;
import jargyle.common.net.SocketFactory;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.KeyManagersFactory;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.net.ssl.TrustManagersFactory;
import jargyle.common.security.EncryptedPassword;
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
		if (!this.properties.getValue(
				PropertySpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socket;
		}
		SSLSocketFactory sslSocketFactory =
				(SSLSocketFactory) SSLSocketFactory.getDefault();
		File keyStoreFile = this.properties.getValue(
				PropertySpec.SSL_KEY_STORE_FILE, File.class);
		File trustStoreFile = this.properties.getValue(
				PropertySpec.SSL_TRUST_STORE_FILE, File.class);
		if (keyStoreFile != null || trustStoreFile != null) {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getDefault();
			} catch (NoSuchAlgorithmException e) {
				throw new AssertionError(e);
			}
			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers = null;
			if (keyStoreFile != null) {
				EncryptedPassword keyStorePassword = 
						this.properties.getValue(
								PropertySpec.SSL_KEY_STORE_PASSWORD, 
								EncryptedPassword.class);
				keyManagers = KeyManagersFactory.getKeyManagers(
						keyStoreFile, keyStorePassword);
			}
			if (trustStoreFile != null) {
				EncryptedPassword trustStorePassword = 
						this.properties.getValue(
								PropertySpec.SSL_TRUST_STORE_PASSWORD, 
								EncryptedPassword.class);
				trustManagers = TrustManagersFactory.getTrustManagers(
						trustStoreFile, trustStorePassword);
			}
			try {
				sslContext.init(keyManagers, trustManagers, new SecureRandom());
			} catch (KeyManagementException e) {
				throw new AssertionError(e);
			}
			sslSocketFactory = sslContext.getSocketFactory();
		}
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket, 
				socksServerUri.getHost(), 
				socksServerUri.getPort(), 
				true); 
		CipherSuites enabledCipherSuites = this.properties.getValue(
				PropertySpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Protocols enabledProtocols = this.properties.getValue(
				PropertySpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
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
