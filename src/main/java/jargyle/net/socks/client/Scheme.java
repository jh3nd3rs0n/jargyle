package jargyle.net.socks.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.help.HelpText;
import jargyle.net.socks.client.v5.Socks5ServerUri;

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
		StringBuilder sb = new StringBuilder();
		List<Scheme> list = Arrays.asList(Scheme.values());
		for (Iterator<Scheme> iterator = list.iterator(); 
				iterator.hasNext();) {
			Scheme value = iterator.next();
			sb.append(value);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected scheme must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
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