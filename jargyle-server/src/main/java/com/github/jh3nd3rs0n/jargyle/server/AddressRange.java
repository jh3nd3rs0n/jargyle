package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl.DomainnameRange;
import com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl.Ipv4AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl.Ipv6AddressRange;

@SingleValueTypeDoc(
		description = "",
		name = "Address Range",
		syntax = "ADDRESS|IP_ADDRESS1-IP_ADDRESS2|regex:REGULAR_EXPRESSION",
		syntaxName = "ADDRESS_RANGE"
)
public abstract class AddressRange {

	public static AddressRange newInstanceFrom(final String s) {
		try {
			return Ipv4AddressRange.newInstanceFrom(s);
		} catch (IllegalArgumentException ignored) {
		}
		try {
			return Ipv6AddressRange.newInstanceFrom(s);
		} catch (IllegalArgumentException ignored) {
		}
		try {
			return DomainnameRange.newInstanceFrom(s);
		} catch (IllegalArgumentException ignored) {
		}
		throw new IllegalArgumentException(String.format(
				"invalid address range: %s", s));
	}
	
	public abstract boolean covers(final String address);
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract String toString();
	
}
