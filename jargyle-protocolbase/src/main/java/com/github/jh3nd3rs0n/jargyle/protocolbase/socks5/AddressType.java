package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AddressType {

	IPV4((byte) 0x01),
	
	DOMAINNAME((byte) 0x03),
	
	IPV6((byte) 0x04);

	public static AddressType valueOfByte(final byte b) {
		for (AddressType addressType : AddressType.values()) {
			if (addressType.byteValue() == b) {
				return addressType;
			}
		}
		String str = Arrays.stream(AddressType.values())
				.map(AddressType::byteValue)
				.map(bv -> UnsignedByte.valueOf(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected address type must be one of the following values: "
				+ "%s. actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
	}
	
	private final byte byteValue;
	
	private AddressType(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}

}
