package jargyle.net.socks.socks5.client;

import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.Scheme;
import jargyle.net.socks.client.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public Socks5Client newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

}
