package jargyle.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketInterfaceSocketAdapter;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.KeyManagersFactory;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.net.ssl.TrustManagersFactory;
import jargyle.common.security.EncryptedPassword;

final class Listener implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(
			Listener.class.getName());
	
	private final Configuration configuration;
	private final ServerSocket serverSocket;
	
	public Listener(
			final ServerSocket serverSock, 
			final Configuration config) {
		this.configuration = config;
		this.serverSocket = serverSock;
	}
	
	@SuppressWarnings("resource")
	public void run() {
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (SocketException e) {
				// closed by SocksServer.stop()
				break;
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						"Error in waiting for a connection", 
						e);
				continue;
			}
			SocketInterface clientSocketInterface = new DirectSocketInterface(
					clientSocket);
			try {
				clientSocketInterface = this.wrapIfSslEnabled(
						clientSocketInterface);
			} catch (IOException e) {
				LOGGER.log(
						Level.WARNING, 
						"Error in wrapping the client socket", 
						e);
				try {
					clientSocketInterface.close();
				} catch (IOException e1) {
					LOGGER.log(
							Level.WARNING, 
							"Error in closing the client socket", 
							e1);
				}
				continue;
			}
			executor.execute(new Worker(
					clientSocketInterface, this.configuration));
		}
		executor.shutdownNow();
	}
	
	private SocketInterface wrapIfSslEnabled(
			final SocketInterface socketInterface) throws IOException {
		Settings settings = this.configuration.getSettings();
		if (!settings.getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socketInterface;
		}
		SSLContext sslContext = null;
		String protocol = settings.getLastValue(
				SettingSpec.SSL_PROTOCOL, String.class);
		try {
			sslContext = SSLContext.getInstance(protocol);
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
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
			throw new IOException(e);
		}
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				new SocketInterfaceSocketAdapter(socketInterface), 
				null, 
				true); 
		CipherSuites enabledCipherSuites = settings.getLastValue(
				SettingSpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Protocols enabledProtocols = settings.getLastValue(
				SettingSpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		if (settings.getLastValue(SettingSpec.SSL_NEED_CLIENT_AUTH, 
				Boolean.class).booleanValue()) {
			sslSocket.setNeedClientAuth(true);
		}
		if (settings.getLastValue(SettingSpec.SSL_WANT_CLIENT_AUTH, 
				Boolean.class).booleanValue()) {
			sslSocket.setWantClientAuth(true);
		}
		return new DirectSocketInterface(sslSocket);
	}

}
