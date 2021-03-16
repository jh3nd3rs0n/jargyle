package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import jargyle.net.NetObjectFactory;
import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class Socks5Client extends SocksClient {

	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	
	public Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		super(serverUri, props);
		// TODO: when DTLS support is implemented, use PropertySpec.DTLS_ENABLED
		this.dtlsDatagramSocketFactory = props.getValue(
				PropertySpec.SSL_ENABLED).booleanValue() ? 
						new DtlsDatagramSocketFactoryImpl() : null;
	}
	
	public DatagramSocket getConnectedDatagramSocket(
			final DatagramSocket datagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		datagramSocket.connect(
				InetAddress.getByName(udpRelayServerHost), udpRelayServerPort);
		if (this.dtlsDatagramSocketFactory == null) {
			return datagramSocket;
		}
		return this.dtlsDatagramSocketFactory.newDatagramSocket(
				datagramSocket, udpRelayServerHost, udpRelayServerPort, true);
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
				PropertySpec.SOCKS5_AUTH_METHODS);
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
			authenticator = Authenticator.valueOfMethod(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		Socket newSocket = authenticator.authenticate(sock, this);
		return newSocket;
	}

	@Override
	public NetObjectFactory newNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
}
