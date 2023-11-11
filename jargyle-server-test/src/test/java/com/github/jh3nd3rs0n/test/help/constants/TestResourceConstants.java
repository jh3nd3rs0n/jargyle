package com.github.jh3nd3rs0n.test.help.constants;

import com.github.jh3nd3rs0n.test.help.TestResource;

public final class TestResourceConstants {
	
	public static final TestResource JARGYLE_SERVER_TEST_CONFIGURATION_FILE = new TestResource(
			"com/github/jh3nd3rs0n/jargyle/server/test/configuration.xml");
	
	public static final TestResource JARGYLE_SERVER_TEST_EMPTY_CONFIGURATION_FILE = new TestResource(
			"com/github/jh3nd3rs0n/jargyle/server/test/empty_configuration.xml");
	
	public static final TestResource JARGYLE_SERVER_TEST_SOCKS5_USERPASSAUTH_ADDED_USER_TO_USERS_FILE = new TestResource(
			"com/github/jh3nd3rs0n/jargyle/server/test/socks5/userpassauth/added_user_to_users");
	
	public static final TestResource JARGYLE_SERVER_TEST_SOCKS5_USERPASSAUTH_EMPTY_USERS_FILE = new TestResource(
			"com/github/jh3nd3rs0n/jargyle/server/test/socks5/userpassauth/empty_users");
	
	public static final TestResource JARGYLE_SERVER_TEST_SOCKS5_USERPASSAUTH_USERS_FILE = new TestResource(
			"com/github/jh3nd3rs0n/jargyle/server/test/socks5/userpassauth/users");

	private TestResourceConstants() { }
	
}
