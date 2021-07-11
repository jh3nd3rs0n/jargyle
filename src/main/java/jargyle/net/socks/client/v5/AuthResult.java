package jargyle.net.socks.client.v5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import jargyle.net.socks.transport.v5.Method;

public abstract class AuthResult {

	public abstract DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws SocketException;
	
	public abstract Method getMethod();
	
	public abstract Socket getSocket();
	
}
