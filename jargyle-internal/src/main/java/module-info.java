/**
 * Defines the internal API used by all modules. Nothing to see here.
 */
module com.github.jh3nd3rs0n.jargyle.internal {
	requires org.slf4j;	
	exports com.github.jh3nd3rs0n.jargyle.internal.annotation to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.concurrent to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.io to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.logging to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.net to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.net.ssl to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.security to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
	exports com.github.jh3nd3rs0n.jargyle.internal.throwable to
			com.github.jh3nd3rs0n.jargyle.cli,
			com.github.jh3nd3rs0n.jargyle.client,
			com.github.jh3nd3rs0n.jargyle.common,
			com.github.jh3nd3rs0n.jargyle.integration.test,
			com.github.jh3nd3rs0n.jargyle.performance.test,
			com.github.jh3nd3rs0n.jargyle.protocolbase,
			com.github.jh3nd3rs0n.jargyle.server;
}