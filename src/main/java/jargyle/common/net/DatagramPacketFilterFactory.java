package jargyle.common.net;

import java.net.Socket;

import javax.net.ssl.SSLSocket;

import jargyle.common.net.socks5.gssapiauth.GssDatagramPacketFilter;
import jargyle.common.net.socks5.gssapiauth.GssSocket;

public final class DatagramPacketFilterFactory {

	public static DatagramPacketFilter newDatagramPacketFilter(
			final Socket socket) {
		if (socket instanceof GssSocket) {
			GssSocket gssSocket = (GssSocket) socket;
			return new GssDatagramPacketFilter(
					gssSocket.getGSSContext(),
					gssSocket.getMessageProp());
		}
		if (socket instanceof SSLSocket) {
			return new DefaultDatagramPacketFilter();
		}
		if (socket.getClass().equals(Socket.class)) {
			return new DefaultDatagramPacketFilter();
		}
		throw new IllegalArgumentException(String.format(
				"unhandled %s: %s",
				Socket.class.getName(),
				socket.getClass().getName()));
	}

	private DatagramPacketFilterFactory() { }
	
}
