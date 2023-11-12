module com.github.jh3nd3rs0n.jargyle.common {
	requires com.github.jh3nd3rs0n.jargyle.internal;
	requires org.slf4j;
	exports com.github.jh3nd3rs0n.jargyle.common.net;
	exports com.github.jh3nd3rs0n.jargyle.common.number;
	exports com.github.jh3nd3rs0n.jargyle.common.security;
	exports com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl to com.github.jh3nd3rs0n.jargyle.common.test;
	exports com.github.jh3nd3rs0n.jargyle.common.string;
}