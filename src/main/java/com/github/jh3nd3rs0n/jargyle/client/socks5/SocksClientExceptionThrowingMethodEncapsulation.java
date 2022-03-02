package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.client.internal.SocksClientExceptionThrowingSocket;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;

final class SocksClientExceptionThrowingMethodEncapsulation 
	extends MethodEncapsulation {

	private final MethodEncapsulation methodEncapsulation;
	private final Socket socket;
	
	public SocksClientExceptionThrowingMethodEncapsulation(
			final Socks5Client socks5Client,
			final MethodEncapsulation methEncapsulation) {
		this.methodEncapsulation = methEncapsulation;
		this.socket = new SocksClientExceptionThrowingSocket(
				socks5Client, methEncapsulation.getSocket());
	}

	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return this.methodEncapsulation.getDatagramSocket(datagramSocket);
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}

}
