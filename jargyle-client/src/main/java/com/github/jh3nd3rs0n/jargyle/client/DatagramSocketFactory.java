package com.github.jh3nd3rs0n.jargyle.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * A factory that creates {@code DatagramSocket}s.
 */
public abstract class DatagramSocketFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public DatagramSocketFactory() { }

    /**
     * Returns the default instance of {@code DatagramSocketFactory}. The
     * default instance creates plain {@code DatagramSocket}s.
     *
     * @return the default instance of {@code DatagramSocketFactory}
     */
    public static DatagramSocketFactory getDefault() {
        return DefaultDatagramSocketFactory.getInstance();
    }

    /**
     * Returns a {@code DatagramSocketFactory}. If the system property
     * {@code socksClient.socksServerUri} is set and the value is valid, a new
     * {@code SocksDatagramSocketFactory} is returned. Otherwise, the default
     * instance of {@code DatagramSocketFactory} from {@link #getDefault()} is
     * returned.
     *
     * @return a {@code DatagramSocketFactory}
     */
    public static DatagramSocketFactory getInstance() {
        SocksDatagramSocketFactory socksDatagramSocketFactory =
                SocksDatagramSocketFactory.newInstance();
        if (socksDatagramSocketFactory != null) {
            return socksDatagramSocketFactory;
        }
        return getDefault();
    }

    /**
     * Returns a new {@code DatagramSocket} bound to any available local port
     * and the wildcard address.
     * <p>
     * Documentation is based from {@link DatagramSocket#DatagramSocket()}.
     * </p>
     *
     * @return a new {@code DatagramSocket} bound to any available local port
     * and the wildcard address
     * @throws SocketException if the {@code DatagramSocket} could not be
     *                         opened or bound
     */
    public abstract DatagramSocket newDatagramSocket() throws SocketException;

    /**
     * Returns a new {@code DatagramSocket} bound to the provided local port
     * and the wildcard address.
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * Documentation is based from {@link DatagramSocket#DatagramSocket(int)}.
     * </p>
     *
     * @param port the provided local port
     * @return a new {@code DatagramSocket} bound to the provided local port
     * and the wildcard address
     * @throws SocketException if the {@code DatagramSocket} could not be
     *                         opened or bound to the provided local port
     */
    public abstract DatagramSocket newDatagramSocket(
            final int port) throws SocketException;

    /**
     * Returns a new {@code DatagramSocket} bound to the provided local port
     * and the provided local {@code InetAddress}.
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * If the provided local {@code InetAddress} is {@code null} or a wildcard
     * address, the {@code DatagramSocket} will be bound to the wildcard
     * address.
     * </p>
     * <p>
     * Documentation is based from
     * {@link DatagramSocket#DatagramSocket(int, InetAddress)}.
     * </p>
     *
     * @param port  the provided local port
     * @param laddr the provided local {@code InetAddress} (can be
     *              {@code null})
     * @return a new {@code DatagramSocket} bound to the provided local port
     * and the provided local {@code InetAddress}
     * @throws SocketException if the {@code DatagramSocket} could not be
     *                         opened or bound to the provided local port
     */
    public abstract DatagramSocket newDatagramSocket(
            final int port, final InetAddress laddr) throws SocketException;

    /**
     * Returns a new {@code DatagramSocket} bound to the provided local
     * {@code SocketAddress} or a new unbound {@code DatagramSocket} if the
     * provided local {@code SocketAddress} is {@code null}.
     * <p>
     * Documentation is based from
     * {@link DatagramSocket#DatagramSocket(SocketAddress)}.
     * </p>
     *
     * @param bindaddr the provided local {@code SocketAddress} (can be
     *                 {@code null})
     * @return a new {@code DatagramSocket} bound to the provided local
     * {@code SocketAddress} or a new unbound {@code DatagramSocket} if the
     * provided local {@code SocketAddress} is {@code null}
     * @throws SocketException if the {@code DatagramSocket} could not be
     *                         opened or bound to the provided local
     *                         {@code SocketAddress}
     */
    public abstract DatagramSocket newDatagramSocket(
            final SocketAddress bindaddr) throws SocketException;

}
