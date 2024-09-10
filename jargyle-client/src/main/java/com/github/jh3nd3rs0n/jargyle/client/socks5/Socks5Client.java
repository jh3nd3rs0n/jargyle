package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.ClientSocketBuilder;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.DtlsDatagramSocketFactoryImpl;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public final class Socks5Client extends SocksClient {

	private final ClientSocketBuilder clientSocketBuilder;
	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;

	Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		this.clientSocketBuilder = new ClientSocketBuilder(this);
        this.dtlsDatagramSocketFactory =
				DtlsDatagramSocketFactoryImpl.isDtlsEnabled(props) ?
						new DtlsDatagramSocketFactoryImpl(props) : null;
	}

	MethodEncapsulation doMethodSubNegotiation(
			final Method method,
			final Socket connectedClientSocket) throws IOException {
		MethodSubNegotiator methodSubNegotiator =
				MethodSubNegotiator.getInstance(method);
		return methodSubNegotiator.subNegotiate(connectedClientSocket, this);
	}

	ClientSocketBuilder getClientSocketBuilder() {
		return this.clientSocketBuilder;
	}

	DatagramSocket getConnectedClientDatagramSocket(
			final DatagramSocket clientDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
				udpRelayServerHost);
		DatagramSocket clientDatagramSock = clientDatagramSocket;
		clientDatagramSock.connect(
				udpRelayServerHostInetAddress, udpRelayServerPort);
		if (this.dtlsDatagramSocketFactory == null) {
			return clientDatagramSock;
		}
		clientDatagramSock = this.dtlsDatagramSocketFactory.newDatagramSocket(
				clientDatagramSock);
		return clientDatagramSock;
	}

	Method negotiateMethod(
			final Socket connectedClientSocket) throws IOException {
		Methods methods = this.getProperties().getValue(
				Socks5PropertySpecConstants.SOCKS5_METHODS);
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(methods);
		InputStream inputStream = connectedClientSocket.getInputStream();
		OutputStream outputStream = connectedClientSocket.getOutputStream();
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		return smsm.getMethod();
	}

	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
	Reply receiveReply(
			final Socket connectedClientSocket) throws IOException {
		InputStream inputStream = connectedClientSocket.getInputStream();
		Reply rep = Reply.newInstanceFrom(inputStream);
		ReplyCode replyCode = rep.getReplyCode();
		if (!replyCode.equals(ReplyCode.SUCCEEDED)) {
			throw new FailureReplyException(this, rep);			
		}
		return rep;		
	}

	void sendRequest(
			final Request req, 
			final Socket connectedClientSocket) throws IOException {
		OutputStream outputStream = connectedClientSocket.getOutputStream();
		outputStream.write(req.toByteArray());
		outputStream.flush();
	}
	
}
