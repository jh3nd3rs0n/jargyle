/**
 * Defines the SOCKS server API.
 */
module com.github.jh3nd3rs0n.jargyle.server {
	requires transitive com.github.jh3nd3rs0n.jargyle.client;
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires transitive com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive com.github.jh3nd3rs0n.jargyle.protocolbase;
	requires jakarta.xml.bind;
	requires org.slf4j;
    exports com.github.jh3nd3rs0n.jargyle.server;
	opens com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind to jakarta.xml.bind;
	exports com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;
}