module com.github.jh3nd3rs0n.jargyle.client {
	requires transitive com.github.jh3nd3rs0n.jargyle.common;
	requires com.github.jh3nd3rs0n.jargyle.internal;
	requires transitive com.github.jh3nd3rs0n.jargyle.protocolbase;
	exports com.github.jh3nd3rs0n.jargyle.client;
	exports com.github.jh3nd3rs0n.jargyle.client.socks5;
}