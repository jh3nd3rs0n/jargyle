package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(
			final UserInfo usrInfo, final Host hst, final Port prt) {
		super(SocksServerUriScheme.SOCKS5, usrInfo, hst, prt);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Socks5ServerUri other = (Socks5ServerUri) obj;
		return this.toURI().equals(other.toURI());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.toURI().hashCode();
		return result;
	}

	@Override
	public SocksClient newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

	@Override
	public SocksClient newSocksClient(
			final Properties properties, final SocksClient chainedSocksClient) {
		return new Socks5Client(this, properties, chainedSocksClient);
	}

}
