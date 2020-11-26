package jargyle.client.socks5;

import jargyle.client.Properties;
import jargyle.client.Scheme;
import jargyle.client.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public Socks5Client newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

}
