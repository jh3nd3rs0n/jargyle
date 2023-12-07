package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;
import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosIpAddressConstants;
import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;

@SingleValueTypeDoc(
		description = "",
		name = "Host",
		syntax = "HOST_NAME|HOST_ADDRESS",
		syntaxName = "HOST"
)
public final class Host {
	
	private static final Host ALL_ZEROS_IPV4_ADDRESS_INSTANCE = Host.newInstance(
			AllZerosIpAddressConstants.IPV4_ADDRESS);
	
	private static final Host ALL_ZEROS_IPV6_ADDRESS_INSTANCE = Host.newInstance(
			AllZerosIpAddressConstants.IPV6_ADDRESS);
	
	public static final Host getAllZerosIpv4AddressInstance() {
		return ALL_ZEROS_IPV4_ADDRESS_INSTANCE;
	}
	
	public static final Host getAllZerosIpv6AddressInstance() {
		return ALL_ZEROS_IPV6_ADDRESS_INSTANCE;
	}
	
	public static Host newInstance(final String s) {
		if (!InetAddressHelper.isInetAddress(s)) {
			throw new IllegalArgumentException(String.format(
					"invalid host name or address: %s", 
					s));
		}
		return new Host(s);
	}

	private final String string;
	
	private Host(final String str) {
		this.string = str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Host other = (Host) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}
	
	public InetAddress toInetAddress() throws UnknownHostException {
		return InetAddress.getByName(string);
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
