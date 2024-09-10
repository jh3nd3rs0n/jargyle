package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.client.socks5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

@EnumValueTypeDoc(
		description = "",
		name = "Scheme",
		syntax = "socks5",
		syntaxName = "SCHEME"
)
public enum Scheme {
	
	@EnumValueDoc(description = "SOCKS5 protocol version 5", value = "socks5")
	SOCKS5("socks5") {

		@Override
		public SocksServerUri newSocksServerUri(final String host) {
			return new Socks5ServerUri(host);
		}

		@Override
		public SocksServerUri newSocksServerUri(
				final String host, final int port) {
			return new Socks5ServerUri(host, port);
		}

		@Override
		public SocksServerUri newSocksServerUri(
				final Host host, final Port port) {
			return new Socks5ServerUri(host, port);
		}

	};

	public static Scheme valueOfString(final String string) {
		for (Scheme scheme : Scheme.values()) {
			if (scheme.toString().equals(string)) {
				return scheme;
			}
		}
		String str = Arrays.stream(Scheme.values())
				.map(Scheme::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected scheme must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				string));			
	}
	
	private final String string;
	
	private Scheme(final String str) {
		this.string = str;
	}

	public abstract SocksServerUri newSocksServerUri(final String host);

	public abstract SocksServerUri newSocksServerUri(
			final String host, final int port);

	public abstract SocksServerUri newSocksServerUri(
			final Host host, final Port port);

	@Override
	public String toString() {
		return this.string;
	}

}