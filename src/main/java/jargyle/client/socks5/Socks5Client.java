package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import jargyle.client.Properties;
import jargyle.client.PropertySpec;
import jargyle.client.SocksClient;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.ClientMethodSelectionMessage;
import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.ServerMethodSelectionMessage;

public final class Socks5Client extends SocksClient {

	public Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		super(serverUri, props);
	}
	
	@Override
	public Socket connectToSocksServerWith(
			final Socket socket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		Socket sock = super.connectToSocksServerWith(
				socket, timeout, bindBeforeConnect);
		InputStream inputStream = sock.getInputStream();
		OutputStream outputStream = sock.getOutputStream();
		Set<Method> methods = new TreeSet<Method>();
		AuthMethods authMethods = this.getProperties().getValue(
				PropertySpec.SOCKS5_AUTH_METHODS, AuthMethods.class);
		for (AuthMethod authMethod : authMethods.toList()) {
			methods.add(authMethod.methodValue());
		}
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(methods);
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		Method method = smsm.getMethod();
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		Socket newSocket = authenticator.authenticate(socket, this);
		return newSocket;
	}

	@Override
	public Socks5DatagramSocketFactory newDatagramSocketFactory() {
		return new Socks5DatagramSocketFactory(this);
	}

	@Override
	public Socks5ServerSocketFactory newServerSocketFactory() {
		return new Socks5ServerSocketFactory(this);
	}

	@Override
	public Socks5SocketFactory newSocketFactory() {
		return new Socks5SocketFactory(this);
	}
	
}
