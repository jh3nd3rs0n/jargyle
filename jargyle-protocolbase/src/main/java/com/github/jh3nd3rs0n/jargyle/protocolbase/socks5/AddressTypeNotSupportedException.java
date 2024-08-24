package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;

/**
 * Thrown when a type of {@code Address} is unknown or unsupported.
 */
public final class AddressTypeNotSupportedException extends Socks5Exception {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@code UnsignedByte} associated with the type of {@code Address}
     * that is unknown or unsupported.
     */
    private final UnsignedByte addressType;

    /**
     * Constructs an {@code AddressTypeNotSupportedException} with the
     * provided {@code UnsignedByte} associated with the type of
     * {@code Address} that is unknown or unsupported.
     *
     * @param addrType the provided {@code UnsignedByte} associated with the
     *                 type of {@code Address} that is unknown or unsupported
     */
    public AddressTypeNotSupportedException(final UnsignedByte addrType) {
        super(String.format(
                "address type not supported: %s",
                Integer.toHexString(addrType.intValue())));
        this.addressType = addrType;
    }

    /**
     * Returns the {@code UnsignedByte} associated with the type of
     * {@code Address} that is unknown or unsupported.
     *
     * @return the {@code UnsignedByte} associated with the type of
     * {@code Address} that is unknown or unsupported
     */
    public UnsignedByte getAddressType() {
        return this.addressType;
    }

}
