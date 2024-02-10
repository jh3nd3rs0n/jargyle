package com.github.jh3nd3rs0n.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;

public final class DatagramEchoClient {
	
	private static final int SO_TIMEOUT = 60000;
	private static final SocketSettings SOCKET_SETTINGS = SocketSettings.of(
			StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
					NonNegativeInteger.valueOf(SO_TIMEOUT)));
	
	private final NetObjectFactory netObjectFactory;
	private final SocketSettings socketSettings;

	public DatagramEchoClient() {
		this(NetObjectFactory.getDefault(), SOCKET_SETTINGS);
	}
	
	public DatagramEchoClient(final NetObjectFactory netObjFactory) {
		this(netObjFactory, SOCKET_SETTINGS);
	}
	
	public DatagramEchoClient(
			final NetObjectFactory netObjFactory, 
			final SocketSettings socketSttngs) {
		this.netObjectFactory = netObjFactory;
		this.socketSettings = socketSttngs;
	}
	
	public String echo(final String string) throws IOException {
		return this.echo(string, DatagramEchoServer.INET_ADDRESS, DatagramEchoServer.PORT);
	}

	public String echo(
			final String string,
			final InetAddress datagramEchoServerInetAddress,
			final int datagramEchoServerPort) throws IOException {
		DatagramSocket datagramSocket = null;
		String returningString = null;
		try {
			datagramSocket = this.netObjectFactory.newDatagramSocket(null);
			this.socketSettings.applyTo(datagramSocket);
			datagramSocket.bind(null);
			datagramSocket.connect(
					datagramEchoServerInetAddress, datagramEchoServerPort);
			byte[] buffer = string.getBytes();
			DatagramPacket packet = new DatagramPacket(
					buffer,
					buffer.length,
					datagramEchoServerInetAddress,
					datagramEchoServerPort);
			datagramSocket.send(packet);
			buffer = new byte[DatagramEchoServer.BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(packet);
			returningString = new String(Arrays.copyOfRange(
					packet.getData(), packet.getOffset(), packet.getLength()));
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		return returningString;
	}

}
