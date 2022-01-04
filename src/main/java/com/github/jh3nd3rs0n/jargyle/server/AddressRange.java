package com.github.jh3nd3rs0n.jargyle.server;

public abstract class AddressRange {

	public static AddressRange newInstance(final String s) {
		if (Ipv4AddressRange.isIpv4AddressRange(s)) {
			return Ipv4AddressRange.newInstance(s);
		}
		if (Ipv6AddressRange.isIpv6AddressRange(s)) {
			return Ipv6AddressRange.newInstance(s);
		}
		if (DomainnameRange.isDomainnameRange(s)) {
			return DomainnameRange.newInstance(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown address range: %s", s));
	}
	
	public abstract boolean contains(final String address);
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract String toString();
	
}
