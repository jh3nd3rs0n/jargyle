package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A factory that creates {@code Socket}s.
 */
public abstract class SocketFactory {

    /**
     * Allows the construction of subclass instances.
     */    
    public SocketFactory() { }

    /**
     * Returns the default instance of {@code SocketFactory}. The default
     * instance creates plain {@code Socket}s.
     *
     * @return the default instance of {@code SocketFactory}
     */
    public static SocketFactory getDefault() {
        return DefaultSocketFactory.getInstance();
    }

    /**
     * Returns a {@code SocketFactory}. If the system property
     * {@code socksClient.socksServerUri} is set and the value is valid, a new
     * {@code SocksSocketFactory} is returned. Otherwise, the default
     * instance of {@code SocketFactory} from {@link #getDefault()} is
     * returned.
     *
     * @return a {@code SocketFactory}
     */
    public static SocketFactory getInstance() {
        SocksSocketFactory socksSocketFactory =
                SocksSocketFactory.newInstance();
        if (socksSocketFactory != null) {
            return socksSocketFactory;
        }
        return getDefault();
    }

    /**
     * Returns a new unconnected {@code Socket}.
     * <p>
     * Documentation is based from {@link Socket#Socket()}.
     * </p>
     *
     * @return a new unconnected {@code Socket}
     */
    public abstract Socket newSocket();

    /**
     * Returns a new {@code Socket} connected to the provided remote
     * {@code InetAddress} and the provided remote port.
     * <p>
     * The provided remote port must be between {@code 0} and {@code 65535}
     * inclusive. An {@code IllegalArgumentException} is thrown if the
     * provided remote port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * Documentation is based from {@link Socket#Socket(InetAddress, int)}.
     * </p>
     *
     * @param address the provided remote {@code InetAddress}
     * @param port    the provided remote port
     * @return a new {@code Socket} connected to the provided remote
     * {@code InetAddress} and the provided remote port
     * @throws IOException if an I/O error occurs in creating the
     *                     {@code Socket}
     */
    public abstract Socket newSocket(
            final InetAddress address, final int port) throws IOException;

    /**
     * Returns a new {@code Socket} connected to the provided remote
     * {@code InetAddress} and the provided remote port and bound to the
     * provided local {@code InetAddress} and the provided local port.
     * <p>
     * The provided remote port must be between {@code 0} and {@code 65535}
     * inclusive. An {@code IllegalArgumentException} is thrown if the
     * provided remote port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * If the provided local {@code InetAddress} is {@code null} or a
     * wildcard address, the {@code Socket} will be bound to the wildcard
     * address.
     * </p>
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * Documentation is based from
     * {@link Socket#Socket(InetAddress, int, InetAddress, int)}.
     * </p>
     *
     * @param address   the provided remote {@code InetAddress}
     * @param port      the provided remote port
     * @param localAddr the provided local {@code InetAddress} (can be
     *                  {@code null})
     * @param localPort the provided local port
     * @return a new {@code Socket} connected to the provided remote
     * {@code InetAddress} and the provided remote port and bound to the
     * provided local {@code InetAddress} and the provided local port
     * @throws IOException if an I/O error occurs in creating the
     *                     {@code Socket}
     */
    public abstract Socket newSocket(
            final InetAddress address,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException;

    /**
     * Returns a new {@code Socket} connected to the provided remote host and
     * the provided remote port.
     * <p>
     * If the provided remote host is {@code null}, the loopback address is
     * used.
     * </p>
     * <p>
     * The provided remote port must be between {@code 0} and {@code 65535}
     * inclusive. An {@code IllegalArgumentException} is thrown if the
     * provided remote port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * Documentation is based from {@link Socket#Socket(String, int)}.
     * </p>
     *
     * @param host the provided remote host (can be {@code null})
     * @param port the provided remote port
     * @return a new {@code Socket} connected to the provided remote host and
     * the provided remote port
     * @throws UnknownHostException if the IP address of the remote host
     *                              cannot be determined
     * @throws IOException          if an I/O error occurs in creating the
     *                              {@code Socket}
     */
    public abstract Socket newSocket(
            final String host,
            final int port) throws UnknownHostException, IOException;

    /**
     * Returns a new {@code Socket} connected to the provided remote host and
     * the provided remote port and bound to the provided local
     * {@code InetAddress} and the provided local port.
     * <p>
     * If the provided remote host is {@code null}, the loopback address is
     * used.
     * </p>
     * <p>
     * The provided remote port must be between {@code 0} and {@code 65535}
     * inclusive. An {@code IllegalArgumentException} is thrown if the
     * provided remote port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * If the provided local {@code InetAddress} is {@code null} or a
     * wildcard address, the {@code Socket} will be bound to the wildcard
     * address.
     * </p>
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * Documentation is based from
     * {@link Socket#Socket(String, int, InetAddress, int)}.
     * </p>
     *
     * @param host      the provided remote host (can be {@code null})
     * @param port      the provided remote port
     * @param localAddr the provided local {@code InetAddress} (can be
     *                  {@code null})
     * @param localPort the provided local port
     * @return a new {@code Socket} connected to the provided remote host and
     * the provided remote port and bound to the provided local
     * {@code InetAddress} and the provided local port
     * @throws UnknownHostException if the IP address of the remote host
     *                              cannot be determined
     * @throws IOException          if an I/O error occurs in creating the
     *                              {@code Socket}
     */
    public abstract Socket newSocket(
            final String host,
            final int port,
            final InetAddress localAddr,
            final int localPort) throws IOException;

}
