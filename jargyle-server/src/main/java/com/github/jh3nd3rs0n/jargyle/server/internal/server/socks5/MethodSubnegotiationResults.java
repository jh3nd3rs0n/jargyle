package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;

final class MethodSubnegotiationResults {

	private final Method method;
	private final MethodEncapsulation methodEncapsulation;
	private final String user;
		
	public MethodSubnegotiationResults(
			final Method meth, 
			final MethodEncapsulation methEncapsulation, 
			final String usr) {
		this.method = meth;
		this.methodEncapsulation = methEncapsulation;
		this.user = usr;
	}
	
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return this.methodEncapsulation.getDatagramSocket(datagramSocket);
	}
	
	public Method getMethod() {
		return this.method;
	}

	public MethodEncapsulation getMethodEncapsulation() {
		return this.methodEncapsulation;
	}
	
	public Socket getSocket() {
		return this.methodEncapsulation.getSocket();
	}
	
	public String getUser() {
		return this.user;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [method=")
			.append(this.method)
			.append(", methodEncapsulation=")
			.append(this.methodEncapsulation)
			.append(", user=")
			.append(this.user)
			.append("]");
		return builder.toString();
	}
	
}
