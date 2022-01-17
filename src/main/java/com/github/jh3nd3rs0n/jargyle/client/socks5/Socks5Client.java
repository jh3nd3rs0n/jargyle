package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.net.ssl.SSLException;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksNetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
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
	protected void configureInternalSocket(
			final Socket internalSocket) throws SocketException {
		super.configureInternalSocket(internalSocket);
	}
	
	protected MethodEncapsulation doMethodSubnegotiation(
			final Method method,
			final Socket connectedInternalSocket) throws IOException {
		MethodEncapsulation methodEncapsulation = null;
		MethodSubnegotiator methodSubnegotiator = 
				MethodSubnegotiator.valueOfMethod(method);
		try {
			methodEncapsulation = methodSubnegotiator.subnegotiate(
					connectedInternalSocket, this);
		} catch (SocketException e) {
			throw e;
		} catch (InterruptedIOException e) {
			throw e;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				throw (SocketException) cause;
			}
			throw new Socks5ClientException(this, e);
		} catch (IOException e) {
			throw new Socks5ClientException(this, e);
		}
		return methodEncapsulation;
	}
	
	protected DatagramSocket getConnectedInternalDatagramSocket(
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
	
	protected Method negotiateMethod(
			final Socket connectedInternalSocket) throws IOException {
		Method method = null;
		try {
			InputStream inputStream = connectedInternalSocket.getInputStream();
			OutputStream outputStream = connectedInternalSocket.getOutputStream();
			Methods methods = this.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_METHODS);
			ClientMethodSelectionMessage cmsm = 
					ClientMethodSelectionMessage.newInstance(methods);
			outputStream.write(cmsm.toByteArray());
			outputStream.flush();
			ServerMethodSelectionMessage smsm =
					ServerMethodSelectionMessage.newInstanceFrom(inputStream);
			method = smsm.getMethod();
		} catch (SocketException e) {
			throw e;
		} catch (InterruptedIOException e) {
			throw e;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				throw (SocketException) cause;
			}
			throw new Socks5ClientException(this, e);
		} catch (IOException e) {
			throw new Socks5ClientException(this, e);
		}
		return method;		
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
	
	protected Socks5Reply receiveSocks5Reply(
			final Socket connectedInternalSocket) throws IOException {
		Socks5Reply socks5Rep = null;
		try {
			InputStream inputStream = connectedInternalSocket.getInputStream();
			socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
		} catch (SocketException e) {
			throw e;
		} catch (InterruptedIOException e) {
			throw e;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				throw (SocketException) cause;
			}
			throw new Socks5ClientException(this, e);
		} catch (IOException e) {
			throw new Socks5ClientException(this, e);
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
			final Socket connectedInternalSocket) throws IOException {
		try {
			OutputStream outputStream = connectedInternalSocket.getOutputStream();
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
		} catch (SocketException e) {
			throw e;
		} catch (InterruptedIOException e) {
			throw e;
		} catch (SSLException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof SocketException) {
				throw (SocketException) cause;
			}
			throw new Socks5ClientException(this, e);			
		} catch (IOException e) {
			throw new Socks5ClientException(this, e);
		}
	}
	
}
