package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;

public final class SocksClientExceptionThrowingDatagramSocket 
	extends FilterDatagramSocket {

	private final SocksClient socksClient;
	
	public SocksClientExceptionThrowingDatagramSocket(
			final SocksClient client, 
			final DatagramSocket datagramSock) throws SocketException {
		super(datagramSock);
		this.socksClient = client;
	}

	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		try {
			this.datagramSocket.receive(p);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		try {
			this.datagramSocket.send(p);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socksClient=")
			.append(this.socksClient)
			.append(", datagramSocket=")
			.append(this.datagramSocket)
			.append("]");
		return builder.toString();
	}

}
