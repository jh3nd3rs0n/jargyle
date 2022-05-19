package com.github.jh3nd3rs0n.jargyle.server;

import java.net.InetAddress;
import java.util.Comparator;

enum InetAddressComparator implements Comparator<InetAddress> {

	INSTANCE;

	@Override
	public int compare(final InetAddress o1, final InetAddress o2) {
		if (o1.getClass() != o2.getClass()) {
			throw new IllegalArgumentException(String.format(
					"cannot compare %s with %s. both must be of the same class.", 
					o1.getClass().getName(),
					o2.getClass().getName()));
		}
		byte[] addr1 = o1.getAddress();
		byte[] addr2 = o2.getAddress();
		for (int i = 0; i < addr1.length; i++) {
			int b1 = addr1[i] & 0xff;
			int b2 = addr2[i] & 0xff;
			int diff = b1 - b2;
			if (diff != 0) {
				return (diff < 0) ? -1 : 1;
			}
		}
		return 0;
	}
	
}
