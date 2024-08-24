package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksNetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public final class Socks5Client extends SocksClient {

	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	
	Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		DtlsDatagramSocketFactory dtlsDatagramSockFactory = 
				DtlsDatagramSocketFactoryImpl.isDtlsEnabled(props) ? 
						new DtlsDatagramSocketFactoryImpl(this) : null;
		this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
	}
	
	@Override
	protected void configureClientSocket(
			final Socket clientSocket) throws SocketException {
		super.configureClientSocket(clientSocket);
	}
	
	protected MethodEncapsulation doMethodSubnegotiation(
			final Method method,
			final Socket connectedClientSocket) throws IOException {
		MethodSubNegotiator methodSubnegotiator =
				MethodSubNegotiator.getInstance(method);
		return methodSubnegotiator.subNegotiate(connectedClientSocket, this);
	}

	protected DatagramSocket getConnectedClientDatagramSocket(
			final DatagramSocket clientDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		DatagramSocket clientDatagramSock = clientDatagramSocket;
		InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
				udpRelayServerHost); 
		clientDatagramSock.connect(
				udpRelayServerHostInetAddress, udpRelayServerPort);
		if (this.dtlsDatagramSocketFactory == null) {
			return clientDatagramSock;
		}
		clientDatagramSock = this.dtlsDatagramSocketFactory.newDatagramSocket(
				clientDatagramSock);
		return clientDatagramSock;
	}
	
	@Override
	protected Socket getConnectedClientSocket(
			final Socket clientSocket, 
			final ClientSocketConnectParams params) throws IOException {
		return super.getConnectedClientSocket(clientSocket, params);
	}
	
	protected Method negotiateMethod(
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
				ServerMethodSelectionMessage.newInstanceFrom(
						inputStream);
		return smsm.getMethod();
	}
	
	@Override
	protected Socket newClientSocket() {
		return super.newClientSocket();
	}
	
	@Override
	protected Socket newConnectedClientSocket() throws IOException {
		return super.newConnectedClientSocket();
	}
	
	@Override
	protected Socket newConnectedClientSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return super.newConnectedClientSocket(localAddr, localPort);
	}
	
	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
	protected Reply receiveReply(
			final Socket connectedClientSocket) throws IOException {
		InputStream inputStream = connectedClientSocket.getInputStream();
		Reply rep = Reply.newInstanceFrom(
				inputStream);
		ReplyCode replyCode = rep.getReplyCode();
		if (!replyCode.equals(ReplyCode.SUCCEEDED)) {
			throw new FailureReplyException(this, rep);			
		}
		return rep;		
	}
	
	@Override
	protected InetAddress resolve(final String host) throws IOException {
		return super.resolve(host);
	}
	
	protected void sendRequest(
			final Request req, 
			final Socket connectedClientSocket) throws IOException {
		OutputStream outputStream = connectedClientSocket.getOutputStream();
		outputStream.write(req.toByteArray());
		outputStream.flush();
	}
	
}
