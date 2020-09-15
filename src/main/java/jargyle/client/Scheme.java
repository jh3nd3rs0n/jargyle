package jargyle.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.client.socks5.Socks5ServerUri;
import jargyle.common.cli.HelpTextParams;

public enum Scheme implements HelpTextParams {

	SOCKS5("socks5") {
		
		private static final String DOC = "SOCKS protocol version 5";

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public SocksServerUri newSocksServerUri(
				final String host, final Integer port) {
			return new Socks5ServerUri(host, port);
		}

	};
	
	public static Scheme getInstance(final String s) {
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

	@Override
	public String getUsage() {
		return this.toString();
	}
	
	@Override
	public boolean isDisplayable() {
		return true;
	}
	
	public abstract SocksServerUri newSocksServerUri(
			final String host, final Integer port);
	
	@Override
	public String toString() {
		return this.string;
	}

}