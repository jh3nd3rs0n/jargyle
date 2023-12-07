package com.github.jh3nd3rs0n.echo.test;

import java.io.IOException;

import com.github.jh3nd3rs0n.echo.EchoServer;

public final class EchoServerHelper {

	public static void startThenExecuteThenStop(
			final EchoServer echoServer, 
			final IoRunnable runnable) throws IOException {
		echoServer.start();
		try {
			runnable.run();
		} finally {
			if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
				echoServer.stop();
			}
		}
	}
	
	private EchoServerHelper() { }
	
}
