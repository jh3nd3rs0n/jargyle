package jargyle.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;

import argmatey.ArgMatey.CLI;
import jargyle.common.net.DefaultServerSocketInterface;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.KeyManagersFactory;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.net.ssl.TrustManagersFactory;
import jargyle.common.security.EncryptedPassword;
import jargyle.common.util.NonnegativeInteger;

public final class SocksServer {
	
	public static void main(final String[] args) {
		CLI socksServerCLI = new SocksServerCLI(args, false);
		int status = socksServerCLI.handleArgs();
		if (status != 0) { System.exit(status);	}
	}
	
	private int backlog;
	private final Configuration configuration;
	private ExecutorService executor;
	private Host host;
	private Port port;
	private ServerSocket serverSocket;
	private SocketSettings socketSettings;
	private boolean started;
	private boolean stopped;
	
	public SocksServer(final Configuration config) {
		this.backlog = config.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.configuration = config;
		this.executor = null;
		this.host = config.getSettings().getLastValue(
				SettingSpec.HOST, Host.class);
		this.port = config.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);
		this.serverSocket = null;
		this.socketSettings = config.getSettings().getLastValue(
				SettingSpec.SOCKET_SETTINGS, SocketSettings.class);
		this.started = false;
		this.stopped = true;
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public Host getHost() {
		return this.host;
	}
	
	public Port getPort() {
		return this.port;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("SocksServer already started");
		}
		this.serverSocket = new ServerSocket();
		Settings settings = this.configuration.getSettings();
		if (settings.getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			SSLContext sslContext = null;
			String protocol = settings.getLastValue(
					SettingSpec.SSL_PROTOCOL, String.class);
			try {
				sslContext = SSLContext.getInstance(protocol);
			} catch (NoSuchAlgorithmException e) {
				throw new AssertionError(e);
			}
			KeyManager[] keyManagers = null;
			TrustManager[] trustManagers = null;
			File keyStoreFile = settings.getLastValue(
					SettingSpec.SSL_KEY_STORE_FILE, File.class);
			if (keyStoreFile != null) {
				EncryptedPassword keyStorePassword = 
						settings.getLastValue(
								SettingSpec.SSL_KEY_STORE_PASSWORD, 
								EncryptedPassword.class);
				String keyStoreType = settings.getLastValue(
						SettingSpec.SSL_KEY_STORE_TYPE, String.class);
				keyManagers = KeyManagersFactory.newKeyManagers(
						keyStoreFile, keyStorePassword, keyStoreType);
			}
			File trustStoreFile = settings.getLastValue(
					SettingSpec.SSL_TRUST_STORE_FILE, File.class);			
			if (trustStoreFile != null) {
				EncryptedPassword trustStorePassword =
						settings.getLastValue(
								SettingSpec.SSL_TRUST_STORE_PASSWORD, 
								EncryptedPassword.class);
				String trustStoreType = settings.getLastValue(
						SettingSpec.SSL_TRUST_STORE_TYPE, String.class);
				trustManagers = TrustManagersFactory.newTrustManagers(
						trustStoreFile, trustStorePassword, trustStoreType);
			}
			try {
				sslContext.init(keyManagers, trustManagers, new SecureRandom());
			} catch (KeyManagementException e) {
				throw new AssertionError(e);
			}
			SSLServerSocketFactory sslServerSocketFactory = 
					sslContext.getServerSocketFactory();
			SSLServerSocket sslServerSocket = 
					(SSLServerSocket) sslServerSocketFactory.createServerSocket();
			CipherSuites enabledCipherSuites = settings.getLastValue(
					SettingSpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
			String[] cipherSuites = enabledCipherSuites.toStringArray();
			if (cipherSuites.length > 0) {
				sslServerSocket.setEnabledCipherSuites(cipherSuites);
			}
			Protocols enabledProtocols = settings.getLastValue(
					SettingSpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
			String[] protocols = enabledProtocols.toStringArray();
			if (protocols.length > 0) {
				sslServerSocket.setEnabledProtocols(protocols);
			}
			if (settings.getLastValue(SettingSpec.SSL_NEED_CLIENT_AUTH, 
					Boolean.class).booleanValue()) {
				sslServerSocket.setNeedClientAuth(true);
			}
			if (settings.getLastValue(SettingSpec.SSL_WANT_CLIENT_AUTH, 
					Boolean.class).booleanValue()) {
				sslServerSocket.setWantClientAuth(true);
			}
			this.serverSocket = sslServerSocket;
		}
		this.socketSettings.applyTo(new DefaultServerSocketInterface(
				this.serverSocket));
		this.serverSocket.bind(new InetSocketAddress(
				this.host.toInetAddress(), this.port.intValue()), this.backlog);
		this.port = Port.newInstance(this.serverSocket.getLocalPort());
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(
				this.serverSocket, this.configuration));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() throws IOException {
		if (this.stopped) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.backlog = this.configuration.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.host = this.configuration.getSettings().getLastValue(
				SettingSpec.HOST, Host.class);
		this.port = this.configuration.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);
		this.socketSettings = this.configuration.getSettings().getLastValue(
				SettingSpec.SOCKET_SETTINGS, SocketSettings.class);
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}

}
