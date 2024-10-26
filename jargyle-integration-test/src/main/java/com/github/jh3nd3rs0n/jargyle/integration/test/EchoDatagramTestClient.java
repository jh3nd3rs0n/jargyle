package com.github.jh3nd3rs0n.jargyle.integration.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;

public final class EchoDatagramTestClient {
	
	private static final int SO_TIMEOUT = 60000;
	private static final SocketSettings SOCKET_SETTINGS = SocketSettings.of(
			StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
					NonNegativeInteger.valueOf(SO_TIMEOUT)));
	
	private final NetObjectFactory netObjectFactory;
	private final SocketSettings socketSettings;

	public EchoDatagramTestClient() {
		this(NetObjectFactory.getDefault(), SOCKET_SETTINGS);
	}
	
	public EchoDatagramTestClient(final NetObjectFactory netObjFactory) {
		this(netObjFactory, SOCKET_SETTINGS);
	}
	
	public EchoDatagramTestClient(
			final NetObjectFactory netObjFactory, 
			final SocketSettings socketSttngs) {
		this.netObjectFactory = netObjFactory;
		this.socketSettings = socketSttngs;
	}
	
	public String echo(
			final String string,
			final int echoTestDatagramServerPort) throws IOException {
		return this.echo(
				string,
				DatagramTestServer.INET_ADDRESS,
				echoTestDatagramServerPort);
	}

	public String echo(
			final String string,
			final InetAddress echoTestDatagramServerInetAddress,
			final int echoTestDatagramServerPort) throws IOException {
		DatagramSocket datagramSocket = null;
		String returningString;
		try {
			datagramSocket = this.netObjectFactory.newDatagramSocket(null);
			this.socketSettings.applyTo(datagramSocket);
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
			buffer = new byte[DatagramTestServer.RECEIVE_BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(packet);
			returningString = new String(
					Arrays.copyOfRange(
							packet.getData(),
							packet.getOffset(),
							packet.getLength()),
					StandardCharsets.UTF_8);
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		return returningString;
	}

}
