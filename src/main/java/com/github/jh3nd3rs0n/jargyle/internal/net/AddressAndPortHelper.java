package com.github.jh3nd3rs0n.jargyle.internal.net;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;

public final class AddressAndPortHelper {

	public static String toString(
			final String address, final int port) {
		if (!AddressHelper.isDomainname(address) 
				&& !AddressHelper.isIpv4Address(address) 
				&& !AddressHelper.isIpv6Address(address)) {
			throw new IllegalArgumentException(String.format(
					"'%s' is not an address", 
					address));
		}
		if (port < 0 || port > Port.MAX_INT_VALUE) {
			throw new IllegalArgumentException("port is out of range");
		}
		String addr = address;
		if (AddressHelper.isIpv6Address(addr)) {
			addr = String.format("[%s]", addr);
		}
		return addr.concat(":").concat(Integer.toString(port));
	}

	public AddressAndPortHelper() { }

}
