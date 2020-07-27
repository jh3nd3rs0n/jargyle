package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

import jargyle.common.net.socks5.gssapiauth.GssDatagramPacketFilter;
import jargyle.common.net.socks5.gssapiauth.GssSocket;

public abstract class DatagramPacketFilter {

	public static DatagramPacketFilter newInstanceFrom(final Socket socket) {
		Class<?> socketClass = socket.getClass();
		if (socketClass.equals(GssSocket.class)) {
			GssSocket gssSocket = (GssSocket) socket;
			return new GssDatagramPacketFilter(
					gssSocket.getGSSContext(),
					gssSocket.getMessageProp());
		} else if (socketClass.equals(Socket.class)) {
			return new DefaultDatagramPacketFilter();
		}
		throw new IllegalArgumentException(String.format(
				"unhandled %s: %s",
				Socket.class.getName(),
				socketClass.getName()));
	}
	
	public abstract void filterAfterReceive(DatagramPacket p) throws IOException;
	
	public abstract void filterBeforeSend(DatagramPacket p) throws IOException;
	
}
