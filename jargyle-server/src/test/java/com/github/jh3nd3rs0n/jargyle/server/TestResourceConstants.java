package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.test.help.resource.Resource;

public final class TestResourceConstants {
	
	public static final Resource JARGYLE_SERVER_CONFIGURATION_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/configuration.xml");
	
	public static final Resource JARGYLE_SERVER_EMPTY_CONFIGURATION_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/empty_configuration.xml");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_ADDED_USER_TO_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassmethod/added_user_to_users");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_EMPTY_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassmethod/empty_users");
	
	public static final Resource JARGYLE_SERVER_SOCKS5_USERPASSMETHOD_USERS_FILE = new Resource(
			TestResourceConstants.class,
            "com/github/jh3nd3rs0n/jargyle/server/socks5/userpassmethod/users");

	private TestResourceConstants() { }
	
}
