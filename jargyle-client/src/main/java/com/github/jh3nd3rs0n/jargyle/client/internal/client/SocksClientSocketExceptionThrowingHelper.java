package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientSocketException;

public class SocksClientSocketExceptionThrowingHelper {

	public static void throwAsSocksClientSocketException(
			final Throwable t,
			final SocksClient socksClient) throws SocksClientSocketException {
		if (t instanceof SocksClientSocketException) {
			throw (SocksClientSocketException) t;
		}
		throw new SocksClientSocketException(socksClient, t);
	}
	
	private SocksClientSocketExceptionThrowingHelper() { }

}
