package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.server.EchoServer;

public final class EchoClient {
	
	private static final int SO_TIMEOUT = 60000;

	public String echoThroughNewServerSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		NetObjectFactory netObjFactory = netObjectFactory;
		if (netObjFactory == null) {
			netObjFactory = new DefaultNetObjectFactory();
		}
		EchoServer echoServer = new EchoServer(
				netObjFactory, 0, EchoServer.BACKLOG);
		Socket echoClient = null;
		String returningString = null;		
		try {
			echoServer.start();
			echoClient = new DefaultNetObjectFactory().newSocket();
			echoClient.setSoTimeout(SO_TIMEOUT);
			echoClient.connect(new InetSocketAddress(
					InetAddress.getLoopbackAddress(), echoServer.getPort()));
			InputStream in = echoClient.getInputStream();
			OutputStream out = echoClient.getOutputStream();
			IoHelper.writeThenFlush(string.getBytes(), out);
			byte[] bytes = IoHelper.readFrom(in);
			returningString = new String(bytes);
		} finally {
			if (echoClient != null) {
				echoClient.close();
			}
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
		}
		return returningString;
	}
	
	public String echoThroughNewSocket(
			final String string, 
			final NetObjectFactory netObjectFactory) throws IOException {
		NetObjectFactory netObjFactory = netObjectFactory;
		if (netObjFactory == null) {
			netObjFactory = new DefaultNetObjectFactory();
		}
		Socket echoClient = null;
		String returningString = null;
		try {
			echoClient = netObjFactory.newSocket();
			echoClient.setSoTimeout(SO_TIMEOUT);
			echoClient.connect(new InetSocketAddress(
					InetAddress.getLoopbackAddress(), EchoServer.PORT));
			InputStream in = echoClient.getInputStream();
			OutputStream out = echoClient.getOutputStream();
			IoHelper.writeThenFlush(string.getBytes(), out);
			byte[] bytes = IoHelper.readFrom(in);
			returningString = new String(bytes);
		} finally {
			if (echoClient != null) {
				echoClient.close();
			}
		}
		return returningString;
	}

}
