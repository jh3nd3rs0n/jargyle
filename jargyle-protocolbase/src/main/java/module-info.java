/**
 * Defines the foundational API for the SOCKS client API and the SOCKS server
 * API.
 */
module com.github.jh3nd3rs0n.jargyle.protocolbase {
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive java.security.jgss;
	exports com.github.jh3nd3rs0n.jargyle.protocolbase;
	exports com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;
	exports com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl;
	exports com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;
	exports com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;
}