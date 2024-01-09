package com.github.jh3nd3rs0n.jargyle.client;

import static com.github.jh3nd3rs0n.jargyle.client.SocksServerUriPropertySpecConstants.HOST;
import static com.github.jh3nd3rs0n.jargyle.client.SocksServerUriPropertySpecConstants.PORT;
import static com.github.jh3nd3rs0n.jargyle.client.SocksServerUriPropertySpecConstants.SCHEME;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

@SingleValueTypeDoc(
		description = "",
		name = "SOCKS Server URI",
		syntax = "SCHEME://HOST[:PORT]",
		syntaxName = "SOCKS_SERVER_URI"
)
public abstract class SocksServerUri {

	public static final int DEFAULT_PORT = 1080;
	
	public static final int MAX_PORT = Port.MAX_INT_VALUE;
	
	public static final int MIN_PORT = 1;
	
	public static SocksServerUri newInstance() {
		String schemeProperty = System.getProperty(SCHEME.getName());
		if (schemeProperty == null) {
			return null;
		}
		Scheme scheme = SCHEME.newPropertyWithParsedValue(
				schemeProperty).getValue();
		String hostProperty = System.getProperty(HOST.getName());
		if (hostProperty == null) {
			return null;
		}
		String host = HOST.newPropertyWithParsedValue(
				hostProperty).getValue().toString();
		String portProperty = System.getProperty(PORT.getName());
		int port = (portProperty == null) ?
				PORT.getDefaultProperty().getValue().intValue()
				: PORT.newPropertyWithParsedValue(
						portProperty).getValue().intValue();
		return scheme.newSocksServerUri(host, Integer.valueOf(port));
	}
	
	public static SocksServerUri newInstanceOf(final String s) {
		URI uri = null;
		try {
			uri = new URI(s);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstance(uri);
	}
	
	public static SocksServerUri newInstance(final URI uri) {
		String message = "URI must be in the following format: "
				+ "SCHEME://HOST[:PORT]";
		String scheme = uri.getScheme();
		if (scheme == null) {
			throw new IllegalArgumentException(message);
		}
		Scheme schm = Scheme.valueOfString(scheme);
		String host = uri.getHost();
		if (host == null) {
			throw new IllegalArgumentException(message);
		}
		String hst = host;
		if (hst.startsWith("[") && hst.endsWith("]")) {
			hst = host.substring(1, host.length() - 1);
		}
		try {
			hst = URLDecoder.decode(hst, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		int port = uri.getPort();
		Integer prt = null;
		if (port != -1) {
			prt = Integer.valueOf(port);
		}
		return schm.newSocksServerUri(hst, prt);
	}
	
	private final String host;
	private final int port;
	private final Scheme scheme;
	private final URI uri;
	
	public SocksServerUri(
			final Scheme schm, final String hst, final Integer prt) {
		Objects.requireNonNull(schm, "scheme must not be null");
		Objects.requireNonNull(hst, "host must not be null or empty");
		if (hst.isEmpty()) {
			throw new IllegalArgumentException("host must not be null or empty");
		}
		Integer p = prt;
		boolean pSet = (p != null);
		if (!pSet) {
			p = Integer.valueOf(DEFAULT_PORT);
		}
		if (p.compareTo(Integer.valueOf(MIN_PORT)) < 0
				|| p.compareTo(Integer.valueOf(MAX_PORT)) > 0) {
			throw new IllegalArgumentException(String.format(
					"port must be an integer between %s and %s (inclusive)", 
					MIN_PORT, MAX_PORT));
		}
		URI u = null;
		try {
			u = new URI(
					schm.toString(), 
					null, 
					hst, 
					(pSet) ? p.intValue() : -1,
					null,
					null,
					null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		this.host = hst;
		this.port = p.intValue();
		this.scheme = schm;
		this.uri = u;
	}
	
	public final String getHost() {
		return this.host;
	}
	
	public final int getPort() {
		return this.port;
	}
	
	public final Scheme getScheme() {
		return this.scheme;
	}
	
	public abstract SocksClient newSocksClient(final Properties properties);
	
	public abstract SocksClient newSocksClient(
			final Properties properties, final SocksClient chainedSocksClient);
	
	@Override
	public final String toString() {
		return this.uri.toString();
	}
	
	public final URI toURI() {
		return this.uri;
	}
}
