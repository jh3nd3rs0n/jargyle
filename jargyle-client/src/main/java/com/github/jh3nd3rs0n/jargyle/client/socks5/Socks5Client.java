package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class Socks5Client extends SocksClient {

	Socks5Client(final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
	}

	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}

	MethodEncapsulation doMethodSubNegotiation(
			final Method method,
			final Socket connectedClientSocket) throws IOException {
		MethodSubNegotiator methodSubNegotiator =
				MethodSubNegotiator.getInstance(method);
		return methodSubNegotiator.subNegotiate(
				connectedClientSocket, this);
	}

	private Methods getMethods() {
		Methods methods = this.getProperties().getValue(
				Socks5PropertySpecConstants.SOCKS5_METHODS);
		UserInfo userInfo = this.getSocksServerUri().getUserInfo();
		if (userInfo == null) {
			return methods;
		}
		UsernamePassword usernamePassword =
				UsernamePassword.tryNewInstanceFrom(userInfo.toString());
		if (usernamePassword == null) {
			return methods;
		}
		if (methods.contains(Method.USERNAME_PASSWORD)) {
			return methods;
		}
		if (methods.equals(Methods.getDefault())) {
			return Methods.of(Method.USERNAME_PASSWORD);
		}
		List<Method> meths = new ArrayList<>();
		meths.add(Method.USERNAME_PASSWORD);
		meths.addAll(methods.toList());
		return Methods.of(meths);
	}

	Method negotiateMethod(
			final Socket connectedClientSocket) throws IOException {
		Methods methods = this.getMethods();
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
