package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class EchoDatagramClient {

	private final DatagramSocketFactory datagramSocketFactory;

	public EchoDatagramClient() {
		this(DatagramSocketFactory.getDefault());
	}

	public EchoDatagramClient(
			final DatagramSocketFactory datagramSockFactory) {
		this.datagramSocketFactory = datagramSockFactory;
	}
	
	public String echo(
			final String string,
			final int echoTestDatagramServerPort) throws IOException {
		return this.echo(
				string,
				DatagramServer.INET_ADDRESS,
				echoTestDatagramServerPort);
	}

	public String echo(
			final String string,
			final InetAddress echoTestDatagramServerInetAddress,
			final int echoTestDatagramServerPort) throws IOException {
		String returningString;
		try (DatagramSocket datagramSocket =
					 this.datagramSocketFactory.newDatagramSocket(null)) {
			datagramSocket.bind(null);
			datagramSocket.connect(
					echoTestDatagramServerInetAddress,
					echoTestDatagramServerPort);
			byte[] buffer = string.getBytes(StandardCharsets.UTF_8);
			DatagramPacket packet = new DatagramPacket(
					buffer,
					buffer.length,
					echoTestDatagramServerInetAddress,
					echoTestDatagramServerPort);
			packet.setLength(buffer.length);
			datagramSocket.send(packet);
			buffer = new byte[DatagramServer.RECEIVE_BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(packet);
			returningString = new String(
					Arrays.copyOfRange(
							packet.getData(),
							packet.getOffset(),
							packet.getLength()),
					StandardCharsets.UTF_8);
		}
		return returningString;
	}

}
