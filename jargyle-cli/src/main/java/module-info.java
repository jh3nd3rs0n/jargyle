/**
 * Defines the Jargyle command line interface.
 */
module com.github.jh3nd3rs0n.jargyle.cli {
	requires com.github.jh3nd3rs0n.argmatey;
	requires com.github.jh3nd3rs0n.jargyle.client;
	requires com.github.jh3nd3rs0n.jargyle.common;
	requires com.github.jh3nd3rs0n.jargyle.internal;
	requires com.github.jh3nd3rs0n.jargyle.protocolbase;
	requires com.github.jh3nd3rs0n.jargyle.server;
	requires org.slf4j;
	exports com.github.jh3nd3rs0n.jargyle.cli;
	opens com.github.jh3nd3rs0n.jargyle.cli to com.github.jh3nd3rs0n.argmatey;
}