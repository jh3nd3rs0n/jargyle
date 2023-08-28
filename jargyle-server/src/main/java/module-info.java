module com.github.jh3nd3rs0n.jargyle.server {
	requires transitive com.github.jh3nd3rs0n.jargyle.client;
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires transitive com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive com.github.jh3nd3rs0n.jargyle.transport;
	requires transitive java.security.jgss;
	requires transitive org.slf4j;
	exports com.github.jh3nd3rs0n.jargyle.server;
	// export FileMonitor, FileStatusListener to XmlFileSourceConfigurationRepository
	exports com.github.jh3nd3rs0n.jargyle.server.internal.io to com.github.jh3nd3rs0n.jargyle.server.configrepo.impl;
	// export Rules to RulesTest
	exports com.github.jh3nd3rs0n.jargyle.server.internal.server to com.github.jh3nd3rs0n.jargyle.server.test;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;
	// export Pbkdf2WithHmacSha256HashedPassword to Pbkdf2WithHmacSha256HashedPasswordTest
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl to com.github.jh3nd3rs0n.jargyle.server.test;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl;
}