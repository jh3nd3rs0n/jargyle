/**
 * Defines the API for integration testing between the SOCKS client API and the
 * SOCKS server API.
 */
module com.github.jh3nd3rs0n.jargyle.integration.test {
    requires com.github.jh3nd3rs0n.jargyle.client;
    requires com.github.jh3nd3rs0n.jargyle.common;
    requires com.github.jh3nd3rs0n.jargyle.internal;
    requires org.slf4j;
    exports com.github.jh3nd3rs0n.jargyle.integration.test;
}