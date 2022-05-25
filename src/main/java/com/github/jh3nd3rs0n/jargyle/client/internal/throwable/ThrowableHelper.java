package com.github.jh3nd3rs0n.jargyle.client.internal.throwable;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientException;

public final class ThrowableHelper {
	
	public static void throwAsSocksClientException(
			final Throwable t,
			final SocksClient socksClient) throws SocksClientException {
		if (t instanceof SocksClientException) {
			throw (SocksClientException) t;
		}
		throw new SocksClientException(socksClient, t);
	}

	private ThrowableHelper() { }
	
}
