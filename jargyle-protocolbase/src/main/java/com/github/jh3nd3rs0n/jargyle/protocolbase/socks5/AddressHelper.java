package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv6Address;
import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.*;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

final class AddressHelper {

    private abstract static class AddressImplHelper {

        private final AddressType addressImplType;

        private AddressImplHelper(final AddressType addrImplType) {
            this.addressImplType = addrImplType;
        }

        public AddressType getAddressImplType() {
            return this.addressImplType;
        }

        public abstract Address newAddress(final String string);

        public abstract Address readAddressFrom(
                final InputStream in) throws IOException;

        public abstract byte[] toByteArray(final Address address);

    }

    private static final class AddressImplHelpers {

        private final Map<AddressType, AddressImplHelper> addressImplHelpersMap;

        public AddressImplHelpers() {
            this.addressImplHelpersMap = new HashMap<>();
        }

        public void add(final AddressImplHelper value) {
            this.addressImplHelpersMap.put(value.getAddressImplType(), value);
        }

        public Map<AddressType, AddressImplHelper> toMap() {
            return Collections.unmodifiableMap(this.addressImplHelpersMap);
        }

    }

    private static final class DomainnameImplHelper extends AddressImplHelper {

        public DomainnameImplHelper() {
            super(AddressType.DOMAINNAME);
        }

        @Override
        public Address newAddress(final String string) {
            Host host = Host.newInstance(string);
            if (!(host instanceof HostName)) {
                throw new IllegalArgumentException(String.format(
                        "invalid address: %s", string));
            }
            return new Address(this.getAddressImplType(), string);
        }

        @Override
        public Address readAddressFrom(
                final InputStream in) throws IOException {
            UnsignedByte octetCount =
                    UnsignedByteIoHelper.readUnsignedByteFrom(in);
            byte[] bytes = new byte[octetCount.intValue()];
            int bytesRead = in.read(bytes);
            if (octetCount.intValue() != bytesRead) {
                throw new EOFException(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        octetCount.intValue(),
                        bytesRead));
            }
            bytes = Arrays.copyOf(bytes, bytes.length);
            String string = new String(bytes);
            if (!(Host.newInstance(string) instanceof HostName)) {
                throw new Socks5Exception(String.format(
                        "invalid address: %s", string));
            }
            return new Address(this.getAddressImplType(), string);
        }

        @Override
        public byte[] toByteArray(final Address address) {
            byte[] bytes = address.toString().getBytes();
            UnsignedByte octetCount = UnsignedByte.valueOf(bytes.length);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(UnsignedByte.valueOf(
                    this.getAddressImplType().byteValue()).intValue());
            out.write(UnsignedByte.valueOf(
                    octetCount.byteValue()).intValue());
            try {
                out.write(bytes);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return out.toByteArray();
        }

    }

    private static final class Ipv4AddressImplHelper extends AddressImplHelper {

        public Ipv4AddressImplHelper() {
            super(AddressType.IPV4);
        }

        @Override
        public Address newAddress(final String string) {
            Host host = Host.newInstance(string);
            if (!(host instanceof HostIpv4Address)) {
                throw new IllegalArgumentException(String.format(
                        "invalid address: %s", string));
            }
            InetAddress inetAddress;
            try {
                inetAddress = host.toInetAddress();
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
            return new Address(
                    this.getAddressImplType(), inetAddress.getHostAddress());
        }

        @Override
        public Address readAddressFrom(
                final InputStream in) throws IOException {
            final int ADDRESS_LENGTH = 4;
            byte[] bytes = new byte[ADDRESS_LENGTH];
            int bytesRead = in.read(bytes);
            if (bytesRead != ADDRESS_LENGTH) {
                throw new EOFException(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        ADDRESS_LENGTH,
                        bytesRead));
            }
            bytes = Arrays.copyOf(bytes, bytesRead);
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByAddress(bytes);
            } catch (UnknownHostException e) {
                throw new Socks5Exception(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        ADDRESS_LENGTH,
                        bytesRead));
            }
            if (!(inetAddress instanceof Inet4Address)) {
                throw new Socks5Exception(String.format(
                        "raw IP address (%s) not IPv4", inetAddress));
            }
            return new Address(
                    this.getAddressImplType(), inetAddress.getHostAddress());
        }

        @Override
        public byte[] toByteArray(final Address address) {
            HostIpv4Address hostIpv4Address =
                    HostIpv4Address.newHostIpv4Address(address.toString());
            InetAddress inetAddress;
            try {
                inetAddress = hostIpv4Address.toInetAddress();
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(UnsignedByte.valueOf(
                    this.getAddressImplType().byteValue()).intValue());
            try {
                out.write(inetAddress.getAddress());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return out.toByteArray();
        }

    }

    private static final class Ipv6AddressImplHelper extends AddressImplHelper {

        public Ipv6AddressImplHelper() {
            super(AddressType.IPV6);
        }

        @Override
        public Address newAddress(final String string) {
            Host host = Host.newInstance(string);
            if (!(host instanceof HostIpv6Address)) {
                throw new IllegalArgumentException(String.format(
                        "invalid address: %s", string));
            }
            InetAddress inetAddress;
            try {
                inetAddress = host.toInetAddress();
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
            return new Address(
                    this.getAddressImplType(), inetAddress.getHostAddress());
        }

        @Override
        public Address readAddressFrom(
                final InputStream in) throws IOException {
            final int ADDRESS_LENGTH = 16;
            byte[] bytes = new byte[ADDRESS_LENGTH];
            int bytesRead = in.read(bytes);
            if (bytesRead != ADDRESS_LENGTH) {
                throw new EOFException(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        ADDRESS_LENGTH,
                        bytesRead));
            }
            bytes = Arrays.copyOf(bytes, bytes.length);
            InetAddress inetAddress;
            try {
                inetAddress = InetAddress.getByAddress(bytes);
            } catch (UnknownHostException e) {
                throw new Socks5Exception(String.format(
                        "expected address length is %s. "
                                + "actual address length is %s",
                        ADDRESS_LENGTH,
                        bytesRead));
            }
            if (!(inetAddress instanceof Inet6Address)) {
                throw new Socks5Exception(String.format(
                        "raw IP address (%s) not IPv6", inetAddress));
            }
            return new Address(
                    this.getAddressImplType(), inetAddress.getHostAddress());
        }

        @Override
        public byte[] toByteArray(final Address address) {
            HostIpv6Address hostIpv6Address =
                    HostIpv6Address.newHostIpv6Address(address.toString());
            InetAddress inetAddress;
            try {
                inetAddress = hostIpv6Address.toInetAddress();
            } catch (UnknownHostException e) {
                throw new AssertionError(e);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(UnsignedByte.valueOf(
                    this.getAddressImplType().byteValue()).intValue());
            try {
                out.write(inetAddress.getAddress());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            return out.toByteArray();
        }
    }

    private static final Map<AddressType, AddressImplHelper> ADDRESS_IMPL_HELPERS_MAP;

    static {
        AddressImplHelpers addressImplHelpers = new AddressImplHelpers();
        addressImplHelpers.add(new DomainnameImplHelper());
        addressImplHelpers.add(new Ipv4AddressImplHelper());
        addressImplHelpers.add(new Ipv6AddressImplHelper());
        ADDRESS_IMPL_HELPERS_MAP = new HashMap<>(addressImplHelpers.toMap());
    }

    private static AddressImplHelper addressImplHelperOf(
            final AddressType addressImplType) {
        AddressImplHelper addressImplHelper = ADDRESS_IMPL_HELPERS_MAP.get(
                addressImplType);
        if (addressImplHelper == null) {
            String str = ADDRESS_IMPL_HELPERS_MAP.values().stream()
                    .map(AddressImplHelper::getAddressImplType)
                    .map(AddressType::toString)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(String.format(
                    "expected AddressType must be one of the following " +
                            "values: %s. actual value is %s",
                    str,
                    addressImplType));
        }
        return addressImplHelper;
    }

    public static Address newAddress(final String string) {
        for (AddressImplHelper addressImplHelper :
                ADDRESS_IMPL_HELPERS_MAP.values()) {
            try {
                return addressImplHelper.newAddress(string);
            } catch (IllegalArgumentException ignored) {
            }
        }
        throw new IllegalArgumentException(String.format(
                "unable to determine address type for the specified address: %s",
                string));
    }

    public static Address readAddressFrom(
            final InputStream in) throws IOException {
        AddressType addressType = readAddressTypeFrom(in);
        AddressImplHelper addressImplHelper = addressImplHelperOf(addressType);
        return addressImplHelper.readAddressFrom(in);
    }

    private static AddressType readAddressTypeFrom(
            final InputStream in) throws IOException {
        UnsignedByte b = UnsignedByteIoHelper.readUnsignedByteFrom(in);
        AddressType addressType;
        try {
            addressType = AddressType.valueOfByte(b.byteValue());
        } catch (IllegalArgumentException e) {
            throw new AddressTypeNotSupportedException(b);
        }
        return addressType;
    }

    public static Address toAddress(final byte[] b) {
        Address address;
        try {
            address = readAddressFrom(new ByteArrayInputStream(b));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return address;
    }

    public static byte[] toByteArray(final Address address) {
        AddressImplHelper addressImplHelper = addressImplHelperOf(
                address.getAddressType());
        return addressImplHelper.toByteArray(address);
    }

    private AddressHelper() { }

}
