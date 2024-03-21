package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.util.Objects;

@SingleValueTypeDoc(
		description = "",
		name = "SOCKS5 Address",
		syntax = "DOMAINNAME|IPV4_ADDRESS|IPV6_ADDRESS",
		syntaxName = "SOCKS5_ADDRESS"
)
public final class Address {

	public static Address newInstance(final String string) {
		return AddressHelper.newAddress(string);
	}
	
	private final AddressType addressType;
	private final String string;
	
	Address(final AddressType type, final String str) {
		this.addressType = Objects.requireNonNull(type);
		this.string = Objects.requireNonNull(str);
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
		Address other = (Address) obj;
		if (!this.addressType.equals(other.addressType)) {
			return false;
		}
		if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.addressType.hashCode();
		result = prime * result + this.string.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return this.string;
	}
	
}
