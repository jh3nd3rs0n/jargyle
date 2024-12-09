package com.github.jh3nd3rs0n.jargyle.common.net;

import java.io.UncheckedIOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * An IPv6 address of a node of a network.
 */
public final class HostIpv6Address extends HostAddress {

    /**
     * The length of a raw IPv6 address in network byte order.
     */
    private static final int ADDRESS_LENGTH = 16;

    /**
     * The regular expression for a blank {@code String}.
     */
    private static final String BLANK_STRING_REGEX = "\\A\\s+\\z";

    /**
     * The regular expression for a hexadecimal value of a 16-bit piece in a
     * IPv6 address.
     */
    private static final String HEX_VALUE_16_BIT_PIECE_REGEX =
            "\\A[a-fA-F0-9]{1,4}\\z";

    /**
     * The regular expression for an IPv4 address in 4 parts.
     */
    private static final String IP_V4_ADDRESS_PIECE_REGEX =
            "\\A[0-9]{1,3}(\\.[0-9]{1,3}){3}\\z";

    /**
     * The start index for the raw IPv4 address in a raw IPv6 address in
     * network byte order.
     */
    private static final int IP_V4_ADDRESS_START_INDEX = 12;

    /**
     * The maximum number of pieces in a IPv6 address without the use of
     * {@code ::} and without the IPv4 address.
     */
    private static final int MAX_PIECE_COUNT = 8;

    /**
     * The regular expression for a non-negative integer.
     */
    private static final String NON_NEGATIVE_INTEGER_REGEX = "\\A\\d+\\z";

    /**
     * Constructs a {@code HostIpv6Address} of the provided IPv6 address
     * and the provided {@code Inet6Address}.
     *
     * @param str      the provided IPv6 address
     * @param inetAddr the provided {@code Inet6Address}
     */
    HostIpv6Address(final String str, final Inet6Address inetAddr) {
        super(str, inetAddr);
    }

    /**
     * Returns the scope ID as an {@code int} from the provided zone
     * identifier. The provided zone identifier must be non-negative integer.
     * An {@code IllegalArgumentException} is thrown if the provided zone
     * identifier is not a non-negative integer.
     *
     * @param zoneIdentifier the provided zone identifier (a non-negative
     *                       integer)
     * @return the scope ID as an {@code int} from the provided zone
     * identifier
     */
    private static int getScopeIdAsIntFrom(final String zoneIdentifier) {
        int scopeId;
        try {
            scopeId = Integer.parseInt(zoneIdentifier);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "invalid scope id: scope id must be an " +
                            "integer between 0 and %s " +
                            "(inclusive). actual scope id is %s",
                    Integer.MAX_VALUE,
                    zoneIdentifier));
        }
        return scopeId;
    }

    /**
     * Returns the scope ID as a {@code NetworkInterface} from the provided
     * zone identifier. An {@code IllegalArgumentException} is thrown if
     * there is no {@code NetworkInterface} of the provided zone identifier.
     * An {@code UncheckedIOException} is thrown if I/O occurs in searching
     * for the {@code NetworkInterface} of the provided zone identifier.
     *
     * @param zoneIdentifier the provided zone identifier
     * @return the scope ID as a {@code NetworkInterface} from the provided
     * zone identifier
     */
    private static NetworkInterface getScopeIdAsNetworkInterfaceFrom(
            final String zoneIdentifier) {
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName(
                    zoneIdentifier);
        } catch (SocketException e) {
            throw new UncheckedIOException(e);
        }
        if (networkInterface == null) {
            throw new IllegalArgumentException(String.format(
                    "unknown scope id: '%s'",
                    zoneIdentifier));
        }
        return networkInterface;
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros.
     *
     * @param string the provided IPv6 address
     * @return a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros
     */
    public static boolean isAllZerosIpv6Address(final String string) {
        InetAddress inetAddress;
        try {
            inetAddress = newInet6AddressFrom(string);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return inetAddress.isAnyLocalAddress();
    }

    /**
     * Returns a new raw IPv6 address in network byte order from the provided
     * {@code String}. An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} is invalid.
     *
     * @param string the provided {@code String}
     * @return a new raw IPv6 address in network byte order from the provided
     * {@code String}
     */
    private static byte[] newAddressFrom(final String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException(
                    "IPv6 address must not be empty");
        }
        if (string.matches(BLANK_STRING_REGEX)) {
            throw new IllegalArgumentException(
                    "IPv6 address must not be blank");
        }
        String[] pieces = string.split(":", -1);
        if (pieces.length > MAX_PIECE_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "number of pieces delimited by ':' must no more than %s. "
                            + "actual number of pieces is %s",
                    MAX_PIECE_COUNT,
                    pieces.length));
        }
        byte[] address = new byte[ADDRESS_LENGTH];
        Arrays.fill(address, (byte) 0x00);
        int offset = 0;
        boolean compressedPresent = false;
        boolean ipv4AddressPresent = false;
        for (int i = 0; i < pieces.length; i++) {
            String piece = pieces[i];
            if (piece.isEmpty()) {
                if (i == 0 && !pieces[i + 1].isEmpty()) {
                    throw new IllegalArgumentException(
                            "first piece cannot be empty if the second piece "
                                    + "is not empty");
                }
                if (i == pieces.length - 1 && !pieces[i - 1].isEmpty()) {
                    throw new IllegalArgumentException(
                            "last piece cannot be empty if the second to last "
                                    + "piece is not empty");
                }
                if (i > 0 && i < pieces.length - 1) {
                    if (compressedPresent) {
                        throw new IllegalArgumentException(
                                "use of '::' can only appear once");
                    }
                    offset = MAX_PIECE_COUNT - pieces.length;
                    compressedPresent = true;
                }
                continue;
            }
            if (piece.matches(HEX_VALUE_16_BIT_PIECE_REGEX)) {
                int hexValue16BitPiece = Integer.parseInt(piece, 16);
                ByteBuffer byteBuffer = ByteBuffer.allocate(Short.BYTES);
                byteBuffer.putShort((short) hexValue16BitPiece);
                byte[] array = byteBuffer.array();
                System.arraycopy(
                        array,
                        0,
                        address,
                        (i + offset) * Short.BYTES,
                        array.length);
                continue;
            }
            if (piece.matches(IP_V4_ADDRESS_PIECE_REGEX)) {
                if (i < pieces.length - 1
                        || pieces.length == MAX_PIECE_COUNT) {
                    throw new IllegalArgumentException(
                            "piece cannot be an IPv4 address if the piece is "
                                    + "not the last and at most the seventh");
                }
                byte[] ipv4Address;
                try {
                    ipv4Address = HostIpv4Address.newAddressFrom(piece);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(String.format(
                            "invalid IPv4 address: %s",
                            piece),
                            e);
                }
                System.arraycopy(
                        ipv4Address,
                        0,
                        address,
                        IP_V4_ADDRESS_START_INDEX,
                        ipv4Address.length);
                ipv4AddressPresent = true;
                continue;
            }
            throw new IllegalArgumentException(String.format(
                    "invalid piece: '%s'",
                    piece));
        }
        if (!compressedPresent) {
            if (!ipv4AddressPresent) {
                if (pieces.length < MAX_PIECE_COUNT) {
                    throw new IllegalArgumentException(String.format(
                            "number of pieces with no use of '::' present and "
                                    + "with no IPv4 address present must be "
                                    + "%s. actual number of pieces is %s",
                            MAX_PIECE_COUNT,
                            pieces.length));
                }
            } else {
                if (pieces.length < MAX_PIECE_COUNT - 1) {
                    throw new IllegalArgumentException(String.format(
                            "number of pieces with no use of '::' present and "
                                    + "with the IPv4 address present must be "
                                    + "%s. actual number of pieces is %s",
                            MAX_PIECE_COUNT - 1,
                            pieces.length));
                }
            }
        }
        return address;
    }

    /**
     * Returns a new {@code HostIpv6Address} of the provided IPv6 address.
     * An {@code IllegalArgumentException} is thrown if the provided
     * IPv6 address is invalid. An {@code UncheckedIOException} is thrown if
     * I/O occurs in searching for the network interface from the zone
     * identifier of the IPv6 address of the provided {@code String}.
     *
     * @param string the provided IPv6 address
     * @return a new {@code HostIpv6Address} of the provided IPv6 address
     */
    public static HostIpv6Address newHostIpv6Address(final String string) {
        return new HostIpv6Address(string, newInet6AddressFrom(string));
    }

    /**
     * Returns a new {@code Inet6Address} from the provided {@code String}.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid. An {@code UncheckedIOException} is thrown if
     * I/O occurs in searching for the network interface from the zone
     * identifier of the IPv6 address of the provided {@code String}.
     *
     * @param string the provided {@code String}
     * @return a new {@code Inet6Address} from the provided {@code String}
     */
    private static Inet6Address newInet6AddressFrom(final String string) {
        String message = String.format(
                "invalid IPv6 address: '%s'",
                string);
        String ipv6Address = string;
        String zoneIdentifier = null;
        int zoneIdentifierDelimiterIndex = string.indexOf('%');
        if (zoneIdentifierDelimiterIndex > -1) {
            ipv6Address = string.substring(0, zoneIdentifierDelimiterIndex);
            zoneIdentifier = string.substring(
                    zoneIdentifierDelimiterIndex + 1);
        }
        byte[] address;
        try {
            address = newAddressFrom(ipv6Address);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        try {
            if (zoneIdentifier == null) {
                return (Inet6Address) InetAddress.getByAddress(address);
            } else {
                if (zoneIdentifier.matches(NON_NEGATIVE_INTEGER_REGEX)) {
                    return Inet6Address.getByAddress(
                            string,
                            address,
                            getScopeIdAsIntFrom(zoneIdentifier));
                }
                return Inet6Address.getByAddress(
                        string,
                        address,
                        getScopeIdAsNetworkInterfaceFrom(zoneIdentifier));
            }
        } catch (IllegalArgumentException | UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
    }

}
