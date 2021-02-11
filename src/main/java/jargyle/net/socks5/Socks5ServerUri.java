package jargyle.net.socks5;

import jargyle.net.socks.Properties;
import jargyle.net.socks.Scheme;
import jargyle.net.socks.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public Socks5Client newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

}
