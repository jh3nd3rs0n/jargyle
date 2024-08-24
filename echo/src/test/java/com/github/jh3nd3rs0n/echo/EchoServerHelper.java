package com.github.jh3nd3rs0n.echo;

import java.io.IOException;

public final class EchoServerHelper {

	public static String startThenEchoThenStop(
			final EchoServer echoServer,
			final EchoClient echoClient,
			final String string) throws IOException {
		String returningString;
		echoServer.start();
		try {
			returningString = echoClient.echo(
					string, echoServer.getInetAddress(), echoServer.getPort());
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
		}
		return returningString;
	}
	
	private EchoServerHelper() { }
	
}
