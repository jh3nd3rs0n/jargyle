package com.github.jh3nd3rs0n.test.help;

public final class TestResourceConstants {

	public static final TestResource ECHO_SOCKS_CLIENT_KEY_STORE_FILE = new TestResource(
			"com/github/jh3nd3rs0n/echo/socks_client.jks");
	
	public static final TestResource ECHO_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE = new TestResource(
			"com/github/jh3nd3rs0n/echo/socks_client.jks.password");
	
	public static final TestResource ECHO_SOCKS_SERVER_KEY_STORE_FILE = new TestResource(
			"com/github/jh3nd3rs0n/echo/socks_server.jks");
	
	public static final TestResource ECHO_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE = new TestResource(
			"com/github/jh3nd3rs0n/echo/socks_server.jks.password");

	private TestResourceConstants() { }
	
}
