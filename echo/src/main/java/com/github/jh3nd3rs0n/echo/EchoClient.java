package com.github.jh3nd3rs0n.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;

public final class EchoClient {
	
	private static final int SO_TIMEOUT = 60000;
	private static final SocketSettings SOCKET_SETTINGS = SocketSettings.newInstance(
			StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
					NonnegativeInteger.newInstance(SO_TIMEOUT)));
	
	private final NetObjectFactory netObjectFactory;
	private final SocketSettings socketSettings;
	
	public EchoClient() {
		this(NetObjectFactory.getDefault(), SOCKET_SETTINGS);
	}
	
	public EchoClient(final NetObjectFactory netObjFactory) {
		this(netObjFactory, SOCKET_SETTINGS); 
	}
	
	public EchoClient(
			final NetObjectFactory netObjFactory, 
			final SocketSettings socketSttngs) {
		this.netObjectFactory = netObjFactory;
		this.socketSettings = socketSttngs; 
	}
	
	public String echo(final String string) throws IOException {
		return this.echo(string, EchoServer.INET_ADDRESS, EchoServer.PORT);
	}
	
	public String echo(
			final String string, 
			final InetAddress echoServerInetAddress,
			final int echoServerPort) throws IOException {
		Socket socket = null;
		String returningString = null;
		try {
			socket = this.netObjectFactory.newSocket();
			this.socketSettings.applyTo(socket);
			socket.connect(new InetSocketAddress(
					echoServerInetAddress, echoServerPort));
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			MeasuredIoHelper.writeThenFlush(string.getBytes(), out);
			byte[] bytes = MeasuredIoHelper.readFrom(in);
			returningString = new String(bytes);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return returningString;		
	}

}
