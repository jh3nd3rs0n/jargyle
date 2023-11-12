package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

public final class AddressTypeNotSupportedException extends Socks5Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final UnsignedByte addressType;
	
	public AddressTypeNotSupportedException(final UnsignedByte addrType) {
		super(String.format(
				"address type not supported: %s", 
				Integer.toHexString(addrType.intValue())));
		this.addressType = addrType;
	}

	public UnsignedByte getAddressType() {
		return this.addressType;
	}

}
