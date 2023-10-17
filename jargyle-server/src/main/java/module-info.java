module com.github.jh3nd3rs0n.jargyle.server {
	requires transitive com.github.jh3nd3rs0n.jargyle.client;
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires transitive com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive com.github.jh3nd3rs0n.jargyle.transport;
	requires com.sun.xml.bind;
	requires com.sun.xml.bind.core;
	requires org.slf4j;	
	exports com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.server.configrepo.impl;
	// export Rules to RulesTest
	exports com.github.jh3nd3rs0n.jargyle.server.internal.server to com.github.jh3nd3rs0n.jargyle.server.test.internal.server;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;
	// export Pbkdf2WithHmacSha256HashedPassword to Pbkdf2WithHmacSha256HashedPasswordTest
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl to com.github.jh3nd3rs0n.jargyle.server.test.socks5.userpassauth.internal.hashedpass.impl;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl;
	// export UsersCsvFileConversionHelper to UsersCsvFileConversionHelperTest
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind to com.github.jh3nd3rs0n.jargyle.server.test.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;
}