package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.client.socks5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public final class SchemeConstants {

	private static final Schemes SCHEMES = new Schemes();
	
	@HelpText(doc = "SOCKS protocol version 5", usage = "socks5")
	public static final Scheme SOCKS5 = SCHEMES.addThenGet(new Scheme(
			"socks5") {

		@Override
		public SocksServerUri newSocksServerUri(
				final String host, final Integer port) {
			return new Socks5ServerUri(host, port);
		}

	});
	
	private static final List<Scheme> VALUES = SCHEMES.toList();
	
	private static final Map<String, Scheme> VALUES_MAP = SCHEMES.toMap();
	
	public static Scheme valueOfString(final String string) {
		if (VALUES_MAP.containsKey(string)) {
			return VALUES_MAP.get(string);
		}
		String str = VALUES.stream()
				.map(Scheme::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected scheme must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				string));		
	}
	
	public static List<Scheme> values() {
		return VALUES;
	}
	
	public static Map<String, Scheme> valuesMap() {
		return VALUES_MAP;
	}
	
	private SchemeConstants() { }
	
}
