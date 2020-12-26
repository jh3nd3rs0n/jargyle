package jargyle.common.net;

import java.net.Socket;

import javax.net.ssl.SSLSocket;

import jargyle.common.net.socks5.gssapiauth.GssDatagramPacketFilter;
import jargyle.common.net.socks5.gssapiauth.GssSocket;

public final class DatagramPacketFilterFactory {

	public static DatagramPacketFilter newDatagramPacketFilter(
			final Socket socket) {
		Class<?> socketClass = socket.getClass();
		if (socketClass.equals(GssSocket.class)) {
			GssSocket gssSocket = (GssSocket) socket;
			return new GssDatagramPacketFilter(
					gssSocket.getGSSContext(),
					gssSocket.getMessageProp());
		}
		if (socketClass.equals(Socket.class)) {
			return new DefaultDatagramPacketFilter();
		}
		if (socketClass.equals(SSLSocket.class)) {
			return new DefaultDatagramPacketFilter();
		}
		throw new IllegalArgumentException(String.format(
				"unhandled %s: %s",
				Socket.class.getName(),
				socketClass.getName()));
	}

	private DatagramPacketFilterFactory() { }
	
}
