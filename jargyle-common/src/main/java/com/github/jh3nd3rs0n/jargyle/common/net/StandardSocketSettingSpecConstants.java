package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

/**
 * Standard {@code SocketSettingSpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "Standard settings applied to a socket",
        name = "Standard Socket Settings"
)
public final class StandardSocketSettingSpecConstants {

    /**
     * The {@code SocketSettingSpecs} of the {@code SocketSettingSpec}
     * constants of this class.
     */
    private static final SocketSettingSpecs SOCKET_SETTING_SPECS =
            new SocketSettingSpecs();

    /**
     * {@code SocketSettingSpec} constant for {@code IP_TOS}: an
     * {@code UnsignedByte} that describes the type-of-service or traffic
     * class field in the IP header for a TCP or UDP socket. The
     * {@code SocketSetting} can only be applied to the following objects:
     * {@code DatagramSocket}, {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The type-of-service or traffic class field in the "
                    + "IP header for a TCP or UDP socket",
            name = "IP_TOS",
            syntax = "IP_TOS=UNSIGNED_BYTE",
            valueType = UnsignedByte.class
    )
    public static final SocketSettingSpec<UnsignedByte> IP_TOS =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "IP_TOS",
                    UnsignedByte.class) {

                @Override
                public void apply(
                        final UnsignedByte value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setTrafficClass(value.intValue());
                }

                @Override
                public void apply(
                        final UnsignedByte value,
                        final Socket socket) throws SocketException {
                    socket.setTrafficClass(value.intValue());
                }

                @Override
                public UnsignedByte extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return UnsignedByte.valueOf(datagramSocket.getTrafficClass());
                }

                @Override
                public UnsignedByte extract(
                        final Socket socket) throws SocketException {
                    return UnsignedByte.valueOf(socket.getTrafficClass());
                }

                @Override
                protected UnsignedByte parse(final String value) {
                    return UnsignedByte.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_BROADCAST}: a
     * {@code Boolean} value to indicate if broadcast datagrams can be sent.
     * The {@code SocketSetting} can only be applied to the following objects:
     * {@code DatagramSocket}.
     */
    @NameValuePairValueSpecDoc(
            description = "Can send broadcast datagrams",
            name = "SO_BROADCAST",
            syntax = "SO_BROADCAST=true|false",
            valueType = Boolean.class
    )
    public static final SocketSettingSpec<Boolean> SO_BROADCAST =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_BROADCAST",
                    Boolean.class) {

                @Override
                public void apply(
                        final Boolean value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setBroadcast(value);
                }

                @Override
                public Boolean extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return datagramSocket.getBroadcast();
                }

                @Override
                protected Boolean parse(final String value) {
                    return Boolean.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_KEEPALIVE}: a
     * {@code Boolean} value to indicate if a TCP socket can be kept alive
     * when no data has been exchanged in either direction. The
     * {@code SocketSetting} can only be applied to the following objects:
     * {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "Keeps a TCP socket alive when no data has been "
                    + "exchanged in either direction",
            name = "SO_KEEPALIVE",
            syntax = "SO_KEEPALIVE=true|false",
            valueType = Boolean.class
    )
    public static final SocketSettingSpec<Boolean> SO_KEEPALIVE =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_KEEPALIVE",
                    Boolean.class) {

                @Override
                public void apply(
                        final Boolean value,
                        final Socket socket) throws SocketException {
                    socket.setKeepAlive(value);
                }

                @Override
                public Boolean extract(
                        final Socket socket) throws SocketException {
                    return socket.getKeepAlive();
                }

                @Override
                protected Boolean parse(final String value) {
                    return Boolean.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_LINGER}: an
     * {@code Integer} of the number of seconds of lingering on closing the
     * TCP socket (disabled if the number of seconds is negative). The
     * {@code SocketSetting} can only be applied on the following objects:
     * {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "Linger on closing the TCP socket in seconds "
                    + "(disabled if the number of seconds is negative)",
            name = "SO_LINGER",
            syntax = "SO_LINGER=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final SocketSettingSpec<Integer> SO_LINGER =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_LINGER",
                    Integer.class) {

                @Override
                public void apply(
                        final Integer value,
                        final Socket socket) throws SocketException {
                    if (value < 0) {
                        socket.setSoLinger(false, value);
                        return;
                    }
                    socket.setSoLinger(true, value);
                }

                @Override
                public Integer extract(
                        final Socket socket) throws SocketException {
                    return socket.getSoLinger();
                }

                @Override
                protected Integer parse(final String value) {
                    return Integer.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_RCVBUF}: a
     * {@code PositiveInteger} of the receive buffer size. The
     * {@code SocketSetting} can only be applied to the following objects:
     * {@code DatagramSocket}, {@code ServerSocket}, {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The receive buffer size",
            name = "SO_RCVBUF",
            syntax = "SO_RCVBUF=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SocketSettingSpec<PositiveInteger> SO_RCVBUF =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_RCVBUF",
                    PositiveInteger.class) {

                @Override
                public void apply(
                        final PositiveInteger value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setReceiveBufferSize(value.intValue());
                }

                @Override
                public void apply(
                        final PositiveInteger value,
                        final ServerSocket serverSocket) throws SocketException {
                    serverSocket.setReceiveBufferSize(value.intValue());
                }

                @Override
                public void apply(
                        final PositiveInteger value,
                        final Socket socket) throws SocketException {
                    socket.setReceiveBufferSize(value.intValue());
                }

                @Override
                public PositiveInteger extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return PositiveInteger.valueOf(
                            datagramSocket.getReceiveBufferSize());
                }

                @Override
                public PositiveInteger extract(
                        final ServerSocket serverSocket) throws SocketException {
                    return PositiveInteger.valueOf(
                            serverSocket.getReceiveBufferSize());
                }

                @Override
                public PositiveInteger extract(
                        final Socket socket) throws SocketException {
                    return PositiveInteger.valueOf(
                            socket.getReceiveBufferSize());
                }

                @Override
                protected PositiveInteger parse(final String value) {
                    return PositiveInteger.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_REUSEADDR}: a
     * {@code Boolean} value to indicate if the socket address and port can be
     * reused. The {@code SocketSetting} can only be applied to the following
     * objects: {@code DatagramSocket}, {@code ServerSocket}, {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "Can reuse socket address and port",
            name = "SO_REUSEADDR",
            syntax = "SO_REUSEADDR=true|false",
            valueType = Boolean.class
    )
    public static final SocketSettingSpec<Boolean> SO_REUSEADDR =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_REUSEADDR",
                    Boolean.class) {

                @Override
                public void apply(
                        final Boolean value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setReuseAddress(value);
                }

                @Override
                public void apply(
                        final Boolean value,
                        final ServerSocket serverSocket) throws SocketException {
                    serverSocket.setReuseAddress(value);
                }

                @Override
                public void apply(
                        final Boolean value,
                        final Socket socket) throws SocketException {
                    socket.setReuseAddress(value);
                }

                @Override
                public Boolean extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return datagramSocket.getReuseAddress();
                }

                @Override
                public Boolean extract(
                        final ServerSocket serverSocket) throws SocketException {
                    return serverSocket.getReuseAddress();
                }

                @Override
                public Boolean extract(
                        final Socket socket) throws SocketException {
                    return socket.getReuseAddress();
                }

                @Override
                protected Boolean parse(final String value) {
                    return Boolean.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_SNDBUF}: a
     * {@code PositiveInteger} of the send buffer size. The
     * {@code SocketSetting} can only be applied to the following objects:
     * {@code DatagramSocket}, {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The send buffer size",
            name = "SO_SNDBUF",
            syntax = "SO_SNDBUF=POSITIVE_INTEGER",
            valueType = PositiveInteger.class
    )
    public static final SocketSettingSpec<PositiveInteger> SO_SNDBUF =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_SNDBUF",
                    PositiveInteger.class) {

                @Override
                public void apply(
                        final PositiveInteger value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setSendBufferSize(value.intValue());
                }

                @Override
                public void apply(
                        final PositiveInteger value,
                        final Socket socket) throws SocketException {
                    socket.setSendBufferSize(value.intValue());
                }

                @Override
                public PositiveInteger extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return PositiveInteger.valueOf(
                            datagramSocket.getSendBufferSize());
                }

                @Override
                public PositiveInteger extract(
                        final Socket socket) throws SocketException {
                    return PositiveInteger.valueOf(socket.getSendBufferSize());
                }

                @Override
                protected PositiveInteger parse(final String value) {
                    return PositiveInteger.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code SO_TIMEOUT}: a
     * {@code NonNegativeInteger} of the timeout in milliseconds on waiting
     * for an idle socket. The {@code SocketSetting} can only be applied to
     * the following objects: {@code DatagramSocket}, {@code ServerSocket},
     * {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "The timeout in milliseconds on waiting for an idle "
                    + "socket",
            name = "SO_TIMEOUT",
            syntax = "SO_TIMEOUT=NON_NEGATIVE_INTEGER",
            valueType = NonNegativeInteger.class
    )
    public static final SocketSettingSpec<NonNegativeInteger> SO_TIMEOUT =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "SO_TIMEOUT",
                    NonNegativeInteger.class) {

                @Override
                public void apply(
                        final NonNegativeInteger value,
                        final DatagramSocket datagramSocket) throws SocketException {
                    datagramSocket.setSoTimeout(value.intValue());
                }

                @Override
                public void apply(
                        final NonNegativeInteger value,
                        final ServerSocket serverSocket) throws SocketException {
                    serverSocket.setSoTimeout(value.intValue());
                }

                @Override
                public void apply(
                        final NonNegativeInteger value,
                        final Socket socket) throws SocketException {
                    socket.setSoTimeout(value.intValue());
                }

                @Override
                public NonNegativeInteger extract(
                        final DatagramSocket datagramSocket) throws SocketException {
                    return NonNegativeInteger.valueOf(
                            datagramSocket.getSoTimeout());
                }

                @Override
                public NonNegativeInteger extract(
                        final ServerSocket serverSocket) {
                    try {
                        return NonNegativeInteger.valueOf(serverSocket.getSoTimeout());
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }

                @Override
                public NonNegativeInteger extract(
                        final Socket socket) throws SocketException {
                    return NonNegativeInteger.valueOf(socket.getSoTimeout());
                }

                @Override
                protected NonNegativeInteger parse(final String value) {
                    return NonNegativeInteger.valueOf(value);
                }

            });

    /**
     * {@code SocketSettingSpec} constant for {@code TCP_NODELAY}: a
     * {@code Boolean} value to indicate disabling Nagle's algorithm. The
     * {@code SocketSetting} can only be applied to the following objects:
     * {@code Socket}.
     */
    @NameValuePairValueSpecDoc(
            description = "Disables Nagle's algorithm",
            name = "TCP_NODELAY",
            syntax = "TCP_NODELAY=true|false",
            valueType = Boolean.class
    )
    public static final SocketSettingSpec<Boolean> TCP_NODELAY =
            SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<>(
                    "TCP_NODELAY",
                    Boolean.class) {

                @Override
                public void apply(
                        final Boolean value,
                        final Socket socket) throws SocketException {
                    socket.setTcpNoDelay(value);
                }

                @Override
                public Boolean extract(
                        final Socket socket) throws SocketException {
                    return socket.getTcpNoDelay();
                }

                @Override
                protected Boolean parse(final String value) {
                    return Boolean.valueOf(value);
                }

            });

    /**
     * Prevents the construction of unnecessary instances.
     */
    private StandardSocketSettingSpecConstants() {
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code SocketSettingSpec}
     * constants.
     *
     * @return an unmodifiable {@code List} of the {@code SocketSettingSpec}
     * constants
     */
    public static List<SocketSettingSpec<Object>> values() {
        return SOCKET_SETTING_SPECS.toList();
    }

    /**
     * Returns an unmodifiable {@code Map} of the {@code SocketSettingSpec}
     * constants each associated by the name they specify for their
     * {@code SocketSetting}.
     *
     * @return an unmodifiable {@code Map} of the {@code SocketSettingSpec}
     * constants each associated by the name they specify for their
     * {@code SocketSetting}
     */
    public static Map<String, SocketSettingSpec<Object>> valuesMap() {
        return SOCKET_SETTING_SPECS.toMap();
    }
}
