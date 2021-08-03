package com.github.jh3nd3rs0n.jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Properties;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksNetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Method;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class Socks5Client extends SocksClient {

	private final Optional<DtlsDatagramSocketFactory> dtlsDatagramSocketFactory;
	
	Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		Optional<DtlsDatagramSocketFactory> dtlsDatagramSockFactory = 
				Optional.ofNullable(
						(props.getValue(PropertySpec.DTLS_ENABLED).booleanValue()) ? 
								new DtlsDatagramSocketFactoryImpl(this) : null);
		this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
	}
	
	@Override
	protected void configureInternalSocket(
			final Socket internalSocket) throws SocketException {
		super.configureInternalSocket(internalSocket);
	}
	
	protected DatagramSocket getConnectedInternalDatagramSocket(
			final DatagramSocket internalDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
				udpRelayServerHost); 
		internalDatagramSocket.connect(
				udpRelayServerHostInetAddress, udpRelayServerPort);
		if (!this.dtlsDatagramSocketFactory.isPresent()) {
			return internalDatagramSocket;
		}
		return this.dtlsDatagramSocketFactory.get().newDatagramSocket(
				internalDatagramSocket,
				udpRelayServerHost,
				udpRelayServerPort);
	}
	
	@Override
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket) throws IOException {
		return super.getConnectedInternalSocket(internalSocket);
	}

	@Override
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final boolean bindBeforeConnect) throws IOException {
		return super.getConnectedInternalSocket(
				internalSocket, bindBeforeConnect);
	}
	
	@Override
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout) throws IOException {
		return super.getConnectedInternalSocket(internalSocket, timeout);
	}
	
	@Override
	protected Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		return super.getConnectedInternalSocket(
				internalSocket, timeout, bindBeforeConnect);
	}
	
	protected MethodEncapsulation negotiateMethod(
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
			throw new AssertionError(e);
		}
		return methodSubnegotiator.subnegotiate(
				connectedInternalSocket, this);		
	}
	
	@Override
	protected Socket newConnectedInternalSocket() throws IOException {
		return super.newConnectedInternalSocket();
	}
	
	@Override
	protected Socket newConnectedInternalSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return super.newConnectedInternalSocket(localAddr, localPort);
	}
	
	@Override
	protected Socket newInternalSocket() {
		return super.newInternalSocket();
	}
	
	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
	@Override
	protected InetAddress resolve(final String host) throws IOException {
		return super.resolve(host);
	}
	
}
