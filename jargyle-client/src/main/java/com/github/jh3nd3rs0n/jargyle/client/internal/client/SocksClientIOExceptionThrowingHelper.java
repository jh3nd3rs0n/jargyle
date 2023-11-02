package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientIOException;

public final class SocksClientIOExceptionThrowingHelper {
	
	public static void throwAsSocksClientIOException(
			final Throwable t,
			final SocksClient socksClient) throws SocksClientIOException {
		if (t instanceof SocksClientIOException) {
			throw (SocksClientIOException) t;
		}
		throw new SocksClientIOException(socksClient, t);
	}

	private SocksClientIOExceptionThrowingHelper() { }
	
}
