package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.test.help.Resource;

public final class TestResourceConstants {

	public static final Resource JARGYLE_INTEGRATION_TEST_SOCKS_CLIENT_KEY_STORE_FILE = new Resource(
            "com/github/jh3nd3rs0n/jargyle/integration/test/socks_client.jks");
	
	public static final Resource JARGYLE_INTEGRATION_TEST_SOCKS_CLIENT_KEY_STORE_PASSWORD_FILE = new Resource(
            "com/github/jh3nd3rs0n/jargyle/integration/test/socks_client.jks.password");
	
	public static final Resource JARGYLE_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE = new Resource(
            "com/github/jh3nd3rs0n/jargyle/integration/test/socks_server.jks");
	
	public static final Resource JARGYLE_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE = new Resource(
            "com/github/jh3nd3rs0n/jargyle/integration/test/socks_server.jks.password");

	private TestResourceConstants() { }
	
}
