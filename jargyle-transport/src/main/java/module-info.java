module com.github.jh3nd3rs0n.jargyle.transport {
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive java.security.jgss;
	exports com.github.jh3nd3rs0n.jargyle.transport;
	exports com.github.jh3nd3rs0n.jargyle.transport.socks5;
	exports com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;
	exports com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth;
}