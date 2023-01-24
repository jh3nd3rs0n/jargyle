package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.github.jh3nd3rs0n.jargyle.server.DatagramEchoServer;

public final class DatagramEchoClient {
	
	private static final int SO_TIMEOUT = 60000;
	
	public String echoThroughNewDatagramSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		NetObjectFactory netObjFactory = netObjectFactory; 
		if (netObjFactory == null) {
			netObjFactory = new DefaultNetObjectFactory();
		}
		DatagramSocket echoClient = null;
		String returningString = null;
		try {
			echoClient = netObjFactory.newDatagramSocket(null);
			echoClient.setSoTimeout(SO_TIMEOUT);
			echoClient.bind(null);
			echoClient.connect(
					InetAddress.getLoopbackAddress(), DatagramEchoServer.PORT);
			byte[] buffer = string.getBytes();
			DatagramPacket packet = new DatagramPacket(
					buffer, 
					buffer.length, 
					InetAddress.getLoopbackAddress(), 
					DatagramEchoServer.PORT);
			echoClient.send(packet);
			buffer = new byte[DatagramEchoServer.BUFFER_SIZE];
			packet = new DatagramPacket(buffer, buffer.length);
			echoClient.receive(packet);
			returningString = new String(Arrays.copyOfRange(
					packet.getData(), packet.getOffset(), packet.getLength()));
		} finally {
			if (echoClient != null) {
				echoClient.close();
			}
		}
		return returningString;
	}

}
