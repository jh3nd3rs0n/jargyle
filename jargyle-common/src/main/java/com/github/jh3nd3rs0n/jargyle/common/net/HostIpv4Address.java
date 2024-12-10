package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * An IPv4 address of a node of a network.
 */
public final class HostIpv4Address extends HostAddress {

    /**
     * The all zeros IPv4 address (as 4 parts).
     */
    public static final String ALL_ZEROS_IPV4_ADDRESS = "0.0.0.0";

    /**
     * The regular expression for a blank {@code String}.
     */
    private static final String BLANK_STRING_REGEX = "\\A\\s+\\z";

    /**
     * The maximum value for part 4 (or for any of the first parts) in an IPv4
     * address.
     */
    private static final long MAX_PART_4_VALUE = 255; // (256 ^ 1) - 1

    /**
     * The maximum value for part 3 in an IPv4 address as 3 parts.
     */
    private static final long MAX_PART_3_VALUE = 65535; // (256 ^ 2) - 1

    /**
     * The maximum value for part 2 in an IPv4 address as 2 parts.
     */
    private static final long MAX_PART_2_VALUE = 16777215; // (256 ^ 3) - 1

    /**
     * The maximum value for part 1 in an IPv4 address as 1 part.
     */
    private static final long MAX_PART_1_VALUE = 4294967295L; // (256 ^ 4) - 1

    /**
     * The maximum number of parts in an IPv4 address.
     */
    private static final int MAX_PART_COUNT = 4;

    /**
     * The index of part 1 of the raw IPv4 address in network byte order.
     */
    private static final int PART_1_INDEX = 0;

    /**
     * The index of part 2 of the raw IPv4 address in network byte order.
     */
    private static final int PART_2_INDEX = 1;

    /**
     * The index of part 3 of the raw IPv4 address in network byte order.
     */
    private static final int PART_3_INDEX = 2;

    /**
     * The index of part 4 of the raw IPv4 address in network byte order.
     */
    private static final int PART_4_INDEX = 3;

    /**
     * The {@code Inet4Address} of an all zeros IPv4 address.
     */
    private static Inet4Address allZerosInet4Address;

    /**
     * The {@code HostIpv4Address} of an all zeros IPv4 address.
     */
    private static HostIpv4Address allZerosInstance;

    /**
     * Constructs a {@code HostIpv4Address} of the provided IPv4 address
     * and the provided {@code Inet4Address}.
     *
     * @param str      the provided IPv4 address
     * @param inetAddr the provided {@code Inet4Address}
     */
    HostIpv4Address(final String str, final Inet4Address inetAddr) {
        super(str, inetAddr);
    }

    /**
     * Returns a {@code HostIpv4Address} of an all zeros IPv4 address.
     *
     * @return a {@code HostIpv4Address} of an all zeros IPv4 address
     */
    public static HostIpv4Address getAllZerosInstance() {
        if (allZerosInstance == null) {
            allZerosInstance = new HostIpv4Address(
                    ALL_ZEROS_IPV4_ADDRESS, getAllZerosInet4Address());
        }
        return allZerosInstance;
    }

    /**
     * Returns an {@code Inet4Address} of an all zeros IPv4 address.
     *
     * @return an {@code Inet4Address} of an all zeros IPv4 address
     */
    public static Inet4Address getAllZerosInet4Address() {
        if (allZerosInet4Address == null) {
            byte[] address = new byte[MAX_PART_COUNT];
            Arrays.fill(address, (byte) 0x00);
            try {
                allZerosInet4Address = (Inet4Address) InetAddress.getByAddress(
                        address);
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
        }
        return allZerosInet4Address;
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * IPv4 address is an IPv4 address of all zeros.
     *
     * @param string the provided IPv4 address
     * @return a {@code boolean} value to indicate if the provided
     * IPv4 address is an IPv4 address of all zeros
     */
    public static boolean isAllZerosIpv4Address(final String string) {
        InetAddress inetAddress;
        try {
            inetAddress = newInet4AddressFrom(string);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return inetAddress.isAnyLocalAddress();
    }

    /**
     * Returns a new raw IPv4 address in network byte order from the provided
     * {@code String}. An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} is invalid.
     *
     * @param string the provided {@code String}
     * @return a new raw IPv4 address in network byte order from the provided
     * {@code String}
     */
    static byte[] newAddressFrom(final String string) {
        if (string.isEmpty()) {
            throw new IllegalArgumentException(
                    "IPv4 address must not be empty");
        }
        if (string.matches(BLANK_STRING_REGEX)) {
            throw new IllegalArgumentException(
                    "IPv4 address must not be blank");
        }
        String[] parts = string.split("\\.", -1);
        int partCount = parts.length;
        if (partCount > MAX_PART_COUNT) {
            throw new IllegalArgumentException(String.format(
                    "number of parts must be no more than %s. "
                            + "actual number of parts is %s",
                    MAX_PART_COUNT,
                    partCount));
        }
        if (partCount == 1) {
            return newAddressFrom1Part(parts);
        }
        if (partCount == 2) {
            return newAddressFrom2Parts(parts);
        }
        if (partCount == 3) {
            return newAddressFrom3Parts(parts);
        }
        return newAddressFrom4Parts(parts);
    }

    /**
     * Returns a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 1 part. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} array of an IPv4 address of 1 part is invalid.
     *
     * @param parts the provided {@code String} array of an IPv4 address of 1
     *              part
     * @return a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 1 part
     */
    private static byte[] newAddressFrom1Part(final String[] parts) {
        long partValue = newPartValueFrom(
                parts[PART_1_INDEX], MAX_PART_1_VALUE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt((int) partValue);
        return byteBuffer.array();
    }

    /**
     * Returns a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 2 parts. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} array of an IPv4 address of 2 parts is invalid.
     *
     * @param parts the provided {@code String} array of an IPv4 address of 2
     *              parts
     * @return a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 2 parts
     */
    private static byte[] newAddressFrom2Parts(final String[] parts) {
        long part1Value = newPartValueFrom(
                parts[PART_1_INDEX], MAX_PART_4_VALUE);
        long part2Value = newPartValueFrom(
                parts[PART_2_INDEX], MAX_PART_2_VALUE);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer1.putInt((int) part1Value);
        byte[] array1 = byteBuffer1.array();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer2.putInt((int) part2Value);
        byte[] array2 = byteBuffer2.array();
        byte[] address = new byte[MAX_PART_COUNT];
        address[PART_1_INDEX] = array1[Integer.BYTES - 1];
        System.arraycopy(array2, 1, address, PART_2_INDEX, 3);
        return address;
    }

    /**
     * Returns a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 3 parts. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} array of an IPv4 address of 3 parts is invalid.
     *
     * @param parts the provided {@code String} array of an IPv4 address of 3
     *              parts
     * @return a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 3 parts
     */
    private static byte[] newAddressFrom3Parts(final String[] parts) {
        long part1Value = newPartValueFrom(
                parts[PART_1_INDEX], MAX_PART_4_VALUE);
        long part2Value = newPartValueFrom(
                parts[PART_2_INDEX], MAX_PART_4_VALUE);
        long part3Value = newPartValueFrom(
                parts[PART_3_INDEX], MAX_PART_3_VALUE);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer1.putInt((int) part1Value);
        byte[] array1 = byteBuffer1.array();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer2.putInt((int) part2Value);
        byte[] array2 = byteBuffer2.array();
        ByteBuffer byteBuffer3 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer3.putInt((int) part3Value);
        byte[] array3 = byteBuffer3.array();
        byte[] address = new byte[MAX_PART_COUNT];
        address[PART_1_INDEX] = array1[Integer.BYTES - 1];
        address[PART_2_INDEX] = array2[Integer.BYTES - 1];
        System.arraycopy(array3, 2, address, PART_3_INDEX, 2);
        return address;
    }

    /**
     * Returns a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 4 parts. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} array of an IPv4 address of 4 parts is invalid.
     *
     * @param parts the provided {@code String} array of an IPv4 address of 4
     *              parts
     * @return a new raw IPv4 address in network byte order from the provided
     * {@code String} array of an IPv4 address of 4 parts
     */
    private static byte[] newAddressFrom4Parts(final String[] parts) {
        long part1Value = newPartValueFrom(
                parts[PART_1_INDEX], MAX_PART_4_VALUE);
        long part2Value = newPartValueFrom(
                parts[PART_2_INDEX], MAX_PART_4_VALUE);
        long part3Value = newPartValueFrom(
                parts[PART_3_INDEX], MAX_PART_4_VALUE);
        long part4Value = newPartValueFrom(
                parts[PART_4_INDEX], MAX_PART_4_VALUE);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer1.putInt((int) part1Value);
        byte[] array1 = byteBuffer1.array();
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer2.putInt((int) part2Value);
        byte[] array2 = byteBuffer2.array();
        ByteBuffer byteBuffer3 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer3.putInt((int) part3Value);
        byte[] array3 = byteBuffer3.array();
        ByteBuffer byteBuffer4 = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer4.putInt((int) part4Value);
        byte[] array4 = byteBuffer4.array();
        byte[] address = new byte[MAX_PART_COUNT];
        address[PART_1_INDEX] = array1[Integer.BYTES - 1];
        address[PART_2_INDEX] = array2[Integer.BYTES - 1];
        address[PART_3_INDEX] = array3[Integer.BYTES - 1];
        address[PART_4_INDEX] = array4[Integer.BYTES - 1];
        return address;
    }

    /**
     * Returns a new {@code HostIpv4Address} of the provided IPv4 address.
     * An {@code IllegalArgumentException} is thrown if the provided
     * IPv4 address is invalid.
     *
     * @param string the provided IPv4 address
     * @return a new {@code HostIpv4Address} of the provided IPv4 address
     */
    public static HostIpv4Address newHostIpv4Address(final String string) {
        return new HostIpv4Address(string, newInet4AddressFrom(string));
    }

    /**
     * Returns a new {@code Inet4Address} from the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid.
     *
     * @param string the provided {@code String}
     * @return a new {@code Inet4Address} from the provided {@code String}
     */
    private static Inet4Address newInet4AddressFrom(final String string) {
        String message = String.format(
                "invalid IPv4 address: '%s'",
                string);
        byte[] address;
        try {
            address = newAddressFrom(string);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        try {
            return (Inet4Address) InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
    }

    /**
     * Returns a new {@code long} value of the part of the IPv4 address from
     * the provided {@code String} of the part of the IPv4 address. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} of the part of the IPv4 address is invalid or if the
     * new {@code long} value of the part of the IPv4 address is greater than
     * the provided maximum {@code long} value of the part of the IPv4 address
     *
     * @param part         the provided {@code String} of the part of the IPv4
     *                     address
     * @param maxPartValue the provided maximum {@code long} value of the part
     *                     of the IPv4 address
     * @return a new {@code long} value of the part of the IPv4 address from
     * the provided {@code String} of the part of the IPv4 address
     */
    private static long newPartValueFrom(
            final String part, final long maxPartValue) {
        long partValue;
        try {
            partValue = Long.parseLong(part);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(
                    "part must be an integer no more than %s. "
                            + "actual part is '%s'",
                    maxPartValue,
                    part));
        }
        if (partValue > maxPartValue) {
            throw new IllegalArgumentException(String.format(
                    "part must be no more than %s. actual part is %s",
                    maxPartValue,
                    partValue));
        }
        return partValue;
    }

}
