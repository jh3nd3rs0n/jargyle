package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;

public final class MethodSubnegotiationResults {

	private final Method method;
	private final MethodEncapsulation methodEncapsulation;
	private final String user;
		
	MethodSubnegotiationResults(
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
	
	public Socket getSocket() {
		return this.methodEncapsulation.getSocket();
	}
	
	public String getUser() {
		return this.user;
	}
	
}
