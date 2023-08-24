package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksNetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientExceptionThrowingDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.socks5.SocksClientExceptionThrowingMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ClientMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.ServerMethodSelectionMessage;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

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
		MethodSubnegotiator methodSubnegotiator =
				MethodSubnegotiator.getInstance(method);
		MethodEncapsulation methodEncapsulation = null;
		try {
			methodEncapsulation = methodSubnegotiator.subnegotiate(
					connectedClientSocket, this);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
		return new SocksClientExceptionThrowingMethodEncapsulation(
				this, methodEncapsulation);
	}

	protected DatagramSocket getConnectedClientDatagramSocket(
			final DatagramSocket clientDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		DatagramSocket clientDatagramSock = clientDatagramSocket;
		try {
			InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
					udpRelayServerHost); 
			clientDatagramSock.connect(
					udpRelayServerHostInetAddress, udpRelayServerPort);
			if (this.dtlsDatagramSocketFactory == null) {
				return new SocksClientExceptionThrowingDatagramSocket(
						this, clientDatagramSock);
			}
			clientDatagramSock = 
					this.dtlsDatagramSocketFactory.newDatagramSocket(
							clientDatagramSock);
			clientDatagramSock = 
					new SocksClientExceptionThrowingDatagramSocket(
							this, clientDatagramSock);
		} catch (UncheckedIOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);			
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
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
		Method method = null;
		try {
			InputStream inputStream = connectedClientSocket.getInputStream();
			OutputStream outputStream = 
					connectedClientSocket.getOutputStream();
			outputStream.write(cmsm.toByteArray());
			outputStream.flush();
			ServerMethodSelectionMessage smsm =
					ServerMethodSelectionMessage.newInstanceFrom(inputStream);
			method = smsm.getMethod();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
		return method;		
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
	
	protected Socks5Reply receiveSocks5Reply(
			final Socket connectedClientSocket) throws IOException {
		Socks5Reply socks5Rep = null;
		try {
			InputStream inputStream = connectedClientSocket.getInputStream();
			socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
		Reply reply = socks5Rep.getReply();
		if (!reply.equals(Reply.SUCCEEDED)) {
			throw new FailureSocks5ReplyException(this, socks5Rep);			
		}
		return socks5Rep;		
	}
	
	@Override
	protected InetAddress resolve(final String host) throws IOException {
		return super.resolve(host);
	}
	
	protected void sendSocks5Request(
			final Socks5Request socks5Req, 
			final Socket connectedClientSocket) throws IOException {
		try {
			OutputStream outputStream = 
					connectedClientSocket.getOutputStream();
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this);
		}
	}
	
}
