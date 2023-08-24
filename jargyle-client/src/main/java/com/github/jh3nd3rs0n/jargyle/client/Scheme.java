package com.github.jh3nd3rs0n.jargyle.client;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.client.socks5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public enum Scheme {

	@HelpText(doc = "SOCKS protocol version 5", usage = "socks5")
	SOCKS5("socks5") {

		@Override
		public SocksServerUri newSocksServerUri(
				final String host, final Integer port) {
			return new Socks5ServerUri(host, port);
		}

	};
	
	public static Scheme valueOfString(final String s) {
		for (Scheme value : Scheme.values()) {
			if (value.toString().equals(s)) {
				return value;
			}
		}
		String str = Arrays.stream(Scheme.values())
				.map(Scheme::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected scheme must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				s));
	}
	
	private final String string;
	
	private Scheme(final String str) {
		this.string = str;
	}
	
	public abstract SocksServerUri newSocksServerUri(
			final String host, final Integer port);
	
	@Override
	public String toString() {
		return this.string;
	}

}