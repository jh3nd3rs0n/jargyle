package jargyle.client.socks5;

import jargyle.client.Scheme;
import jargyle.client.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public Socks5Client.Builder newSocksClientBuilder() {
		return new Socks5Client.Builder(this);
	}

}
