package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.client.SocksNetObjectFactory;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class Socks5Client extends SocksClient {

	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	
	public Socks5Client(
			final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	public Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		DtlsDatagramSocketFactory dtlsDatagramSockFactory =
				(props.getValue(PropertySpec.DTLS_ENABLED).booleanValue()) ?
						new DtlsDatagramSocketFactoryImpl(this) : null;
		this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
	}
	
	public DatagramSocket getConnectedInternalDatagramSocket(
			final DatagramSocket internalDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
				udpRelayServerHost); 
		internalDatagramSocket.connect(
				udpRelayServerHostInetAddress, udpRelayServerPort);
		if (this.dtlsDatagramSocketFactory == null) {
			return internalDatagramSocket;
		}
		return this.dtlsDatagramSocketFactory.newDatagramSocket(
				internalDatagramSocket,
				udpRelayServerHost,
				udpRelayServerPort);
	}
	
	public MethodSubnegotiationResult negotiateUsing(
			final Socket connectedInternalSocket) throws IOException {
		InputStream inputStream = connectedInternalSocket.getInputStream();
		OutputStream outputStream = connectedInternalSocket.getOutputStream();
		Methods methods = this.getProperties().getValue(
				PropertySpec.SOCKS5_METHODS);
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(methods);
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		Method method = smsm.getMethod();
		MethodSubnegotiator methodSubnegotiator = null;
		try {
			methodSubnegotiator = MethodSubnegotiator.valueOfMethod(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		return methodSubnegotiator.subnegotiateUsing(
				connectedInternalSocket, this);
	}
	
	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
}
