package com.github.jh3nd3rs0n.test.help.constants;

import com.github.jh3nd3rs0n.test.help.Resource;

public final class TestResourceConstants {

	public static final Resource ECHO_SOCKS_CLIENT_KEY_STORE_FILE = new Resource(
			"com/github/jh3nd3rs0n/echo/socks_client.jks");
	
	public static final Resource ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE = new Resource(
			"com/github/jh3nd3rs0n/echo/socks_client.jks.password");
	
	public static final Resource ECHO_SOCKS_SERVER_KEY_STORE_FILE = new Resource(
			"com/github/jh3nd3rs0n/echo/socks_server.jks");
	
	public static final Resource ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE = new Resource(
			"com/github/jh3nd3rs0n/echo/socks_server.jks.password");

	private TestResourceConstants() { }
	
}
