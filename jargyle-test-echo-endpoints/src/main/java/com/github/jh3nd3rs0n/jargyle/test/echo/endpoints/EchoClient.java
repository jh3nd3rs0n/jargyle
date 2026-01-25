package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class EchoClient {

	private static final int BUFFER_SIZE = 1024;

	private final SocketFactory socketFactory;

	public EchoClient() {
		this(SocketFactory.getDefault());
	}

	public EchoClient(final SocketFactory sockFactory) {
		this.socketFactory = sockFactory;
	}
	
	public String echo(
			final String string,
			final int echoServerPort) throws IOException {
		return this.echo(string, Server.INET_ADDRESS, echoServerPort);
	}
	
	public String echo(
			final String string, 
			final InetAddress echoServerInetAddress,
			final int echoServerPort) throws IOException {
		String returningString;
		try (Socket socket = this.socketFactory.newSocket()) {
			socket.connect(new InetSocketAddress(
					echoServerInetAddress, echoServerPort));
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			byte[] bytesToSend = string.getBytes(StandardCharsets.UTF_8);
			out.write(bytesToSend);
			out.flush();
			ByteArrayOutputStream bytesReceivedOut = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int length;
			while ((length = in.read(buffer)) != -1) {
				bytesReceivedOut.write(buffer, 0, length);
				if (bytesReceivedOut.size() >= bytesToSend.length) {
					break;
				}
			}
			byte[] bytesReceived = bytesReceivedOut.toByteArray();
            returningString = new String(bytesReceived, StandardCharsets.UTF_8);
		}
		return returningString;		
	}

}
