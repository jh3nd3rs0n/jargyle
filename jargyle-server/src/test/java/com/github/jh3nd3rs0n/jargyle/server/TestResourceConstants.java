package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.test.help.resource.Resource;

public final class TestResourceConstants {
	
	public static final Resource JARGYLE_SERVER_CONFIGURATION_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/configuration.xml");
	
	public static final Resource JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/empty_configuration.xml");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSAUTHMETHOD_ADDED_USER_TO_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassauthmethod/added_user_to_users");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSAUTHMETHOD_EMPTY_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassauthmethod/empty_users");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSAUTHMETHOD_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassauthmethod/users");

	private TestResourceConstants() { }
	
}
