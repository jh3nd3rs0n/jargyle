package jargyle.net.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import jargyle.net.DatagramSocketInterfaceFactory;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.ServerSocketInterfaceFactory;
import jargyle.net.SocketInterface;
import jargyle.net.SocketInterfaceFactory;
import jargyle.net.socks.Properties;
import jargyle.net.socks.PropertySpec;
import jargyle.net.socks.SocksClient;

public final class Socks5Client extends SocksClient {

	public Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		super(serverUri, props);
	}
	
	@Override
	public SocketInterface connectToSocksServerWith(
			final SocketInterface socketInterface, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		SocketInterface sockInterface = super.connectToSocksServerWith(
				socketInterface, timeout, bindBeforeConnect);
		InputStream inputStream = sockInterface.getInputStream();
		OutputStream outputStream = sockInterface.getOutputStream();
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
		ClientSideAuthenticator clientSideAuthenticator = null;
		try {
			clientSideAuthenticator = ClientSideAuthenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		SocketInterface newSocketInterface = 
				clientSideAuthenticator.authenticate(sockInterface, this);
		return newSocketInterface;
	}

	@Override
	public DatagramSocketInterfaceFactory newDatagramSocketInterfaceFactory() {
		return new Socks5DatagramSocketInterfaceFactory(this);
	}

	@Override
	public HostnameResolverFactory newHostnameResolverFactory() {
		return new Socks5HostnameResolverFactory(this);
	}

	@Override
	public ServerSocketInterfaceFactory newServerSocketInterfaceFactory() {
		return new Socks5ServerSocketInterfaceFactory(this);
	}

	@Override
	public SocketInterfaceFactory newSocketInterfaceFactory() {
		return new Socks5SocketInterfaceFactory(this);
	}
	
}
