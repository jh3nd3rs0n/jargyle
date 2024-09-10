package com.github.jh3nd3rs0n.jargyle.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

@SingleValueTypeDoc(
		description = "",
		name = "SOCKS Server URI",
		syntax = "SCHEME://HOST[:PORT]",
		syntaxName = "SOCKS_SERVER_URI"
)
public abstract class SocksServerUri {

	public static final int DEFAULT_PORT_INT_VALUE = 1080;

	public static final Port DEFAULT_PORT = Port.valueOf(
			DEFAULT_PORT_INT_VALUE);

	private static final int UNDEFINED_PORT_INT_VALUE = -1;

	public static SocksServerUri newInstance() {
		String schemeProperty = System.getProperty(
				SocksServerUriPropertySpecConstants.SCHEME.getName());
		if (schemeProperty == null) {
			return null;
		}
		Scheme scheme = SocksServerUriPropertySpecConstants.SCHEME.newPropertyWithParsedValue(
				schemeProperty).getValue();
		String hostProperty = System.getProperty(
				SocksServerUriPropertySpecConstants.HOST.getName());
		if (hostProperty == null) {
			return null;
		}
		Host host = SocksServerUriPropertySpecConstants.HOST.newPropertyWithParsedValue(
				hostProperty).getValue();
		String portProperty = System.getProperty(
				SocksServerUriPropertySpecConstants.PORT.getName());
		Port port = (portProperty == null) ?
				null
				: SocksServerUriPropertySpecConstants.PORT.newPropertyWithParsedValue(
						portProperty).getValue();
		return scheme.newSocksServerUri(host, port);
	}
	
	public static SocksServerUri newInstanceFrom(final String s) {
		URI uri;
		try {
			uri = new URI(s);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		return newInstanceFrom(uri);
	}
	
	public static SocksServerUri newInstanceFrom(final URI uri) {
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
		if (host.startsWith("[") && host.endsWith("]")) {
			host = host.substring(1, host.length() - 1);
		}
		int port = uri.getPort();
		return (port == UNDEFINED_PORT_INT_VALUE) ?
				schm.newSocksServerUri(host)
				: schm.newSocksServerUri(host, port);
	}
	
	private final Host host;
	private final Port port;
	private final Scheme scheme;
	private final URI uri;

	public SocksServerUri(final Scheme schm, final String hst) {
		this(schm, Host.newInstance(hst), null);
	}

	public SocksServerUri(final Scheme schm, final String hst, final int prt) {
		this(schm, Host.newInstance(hst), Port.valueOf(prt));
	}

	public SocksServerUri(
			final Scheme schm, final Host hst, final Port prt) {
		Objects.requireNonNull(schm, "scheme must not be null");
		Objects.requireNonNull(hst, "host must not be null");
		URI u;
		try {
			u = new URI(
					schm.toString(), 
					null, 
					hst.toString(),
					(prt != null) ? prt.intValue() : UNDEFINED_PORT_INT_VALUE,
					null,
					null,
					null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		this.host = hst;
		this.port = prt;
		this.scheme = schm;
		this.uri = u;
	}
	
	public final Host getHost() {
		return this.host;
	}

	public final Port getPort() {
		return this.port;
	}

	public final Port getPortOrDefault() {
		return (this.port != null) ? this.port : DEFAULT_PORT;
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
