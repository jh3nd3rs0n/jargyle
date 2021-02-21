package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import jargyle.net.NetFactory;
import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;

public final class Socks5Client extends SocksClient {

	public Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		super(serverUri, props);
	}
	
	@Override
	public Socket getConnectedSocket(
			final Socket socket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		Socket sock = super.getConnectedSocket(
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
		Socket newSocket = authenticator.authenticate(sock, this);
		return newSocket;
	}

	@Override
	public NetFactory newNetFactory() {
		return new Socks5NetFactory(this);
	}
	
}
