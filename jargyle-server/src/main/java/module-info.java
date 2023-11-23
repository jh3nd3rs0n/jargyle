/**
 * Defines the SOCKS server API.
 */
module com.github.jh3nd3rs0n.jargyle.server {
	requires transitive com.github.jh3nd3rs0n.jargyle.client;
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires transitive com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive com.github.jh3nd3rs0n.jargyle.protocolbase;
	requires com.sun.xml.bind;
	requires com.sun.xml.bind.core;
	requires org.slf4j;	
	exports com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.server.internal.server to com.github.jh3nd3rs0n.server.test;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;
}