package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.*;

/**
 * A factory that creates networking objects such as {@code DatagramSocket}s,
 * {@code HostResolver}s, {@code ServerSocket}s, and {@code Socket}s.
 */
public abstract class NetObjectFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public NetObjectFactory() {
    }

    /**
     * Returns the default instance of {@code NetObjectFactory}. The default
     * instance creates plain networking objects.
     *
     * @return the default instance of {@code NetObjectFactory}
     */
    public static NetObjectFactory getDefault() {
        return DefaultNetObjectFactory.getInstance();
    }

    /**
     * Returns a {@code NetObjectFactory}. If the system properties defined by
     * {@link SocksServerUriPropertySpecConstants#SCHEME} and
     * {@link SocksServerUriPropertySpecConstants#HOST} are set, a new
     * {@code SocksNetObjectFactory} is returned. Otherwise, the default
     * instance of {@code NetObjectFactory} from {@link #getDefault()} is
     * returned.
     *
     * @return a {@code NetObjectFactory}
     */
    public static NetObjectFactory getInstance() {
        NetObjectFactory netObjectFactory = SocksNetObjectFactory.newInstance();
        if (netObjectFactory != null) {
            return netObjectFactory;
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

    /**
     * Returns a new {@code HostResolver}.
     *
     * @return a new {@code HostResolver}
     */
    public abstract HostResolver newHostResolver();

    /**
     * Returns a new unbound {@code ServerSocket}.
     * <p>
     * Documentation is based from {@link ServerSocket#ServerSocket()}.
     * </p>
     *
     * @return a new unbound {@code ServerSocket}
     * @throws IOException if an I/O error occurs in opening the
     *                     {@code ServerSocket}
     */
    public abstract ServerSocket newServerSocket() throws IOException;

    /**
     * Returns a new {@code ServerSocket} bound to the provided local port.
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * The maximum length of the queue of incoming connections is set to
     * {@code 50}. If an incoming connection indication (a request to connect)
     * arrives when the queue is full, the connection is refused. However, the
     * exact semantics are implementation specific. In particular, an
     * implementation may impose a maximum length or may choose to ignore the
     * maximum length altogether.
     * </p>
     * <p>
     * Documentation is based from {@link ServerSocket#ServerSocket(int)}.
     * </p>
     *
     * @param port the provided local port
     * @return a new {@code ServerSocket} bound to the provided local port
     * @throws IOException if an I/O error occurs in opening the
     *                     {@code ServerSocket}
     */
    public abstract ServerSocket newServerSocket(
            final int port) throws IOException;

    /**
     * Returns a new {@code ServerSocket} bound to the provided local port
     * with the provided maximum length of the queue of incoming connections.
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * The maximum length of the queue of incoming connections is set to the
     * provided maximum length of the queue of incoming connections. If an
     * incoming connection indication (a request to connect) arrives when the
     * queue is full, the connection is refused. However, the exact semantics
     * are implementation specific. In particular, an implementation may
     * impose a maximum length or may choose to ignore the maximum length
     * altogether. The value provided should be greater than {@code 0}. If it
     * is less than or equal to {@code 0}, then an implementation specific
     * default will be used.
     * </p>
     * <p>
     * Documentation is based from {@link ServerSocket#ServerSocket(int, int)}.
     * </p>
     *
     * @param port    the provided local port
     * @param backlog the provided maximum length of the queue of incoming
     *                connections
     * @return a new {@code ServerSocket} bound to the provided local port
     * with the provided maximum length of the queue of incoming connections
     * @throws IOException if an I/O error occurs in opening the
     *                     {@code ServerSocket}
     */
    public abstract ServerSocket newServerSocket(
            final int port, final int backlog) throws IOException;

    /**
     * Returns a new {@code ServerSocket} bound to the provided local port and
     * the provided local {@code InetAddress} with the provided maximum length
     * of the queue of incoming connections.
     * <p>
     * The provided local port must be between {@code 0} and {@code 65535}
     * inclusive. A provided local port of {@code 0} will let the system pick
     * up an available port. An {@code IllegalArgumentException} is thrown if
     * the provided local port is less than {@code 0} or greater than
     * {@code 65535}.
     * </p>
     * <p>
     * The maximum length of the queue of incoming connections is set to the
     * provided maximum length of the queue of incoming connections. If an
     * incoming connection indication (a request to connect) arrives when the
     * queue is full, the connection is refused. However, the exact semantics
     * are implementation specific. In particular, an implementation may
     * impose a maximum length or may choose to ignore the maximum length
     * altogether. The value provided should be greater than {@code 0}. If it
     * is less than or equal to {@code 0}, then an implementation specific
     * default will be used.
     * </p>
     * <p>
     * The provided local {@code InetAddress} can be used on a multi-homed
     * host for a {@code ServerSocket} that will only accept connect requests
     * to one of its addresses. If the provided local {@code InetAddress} is
     * {@code null}, it will default accepting connections on any/all local
     * addresses.
     * </p>
     * <p>
     * Documentation is based from
     * {@link ServerSocket#ServerSocket(int, int, InetAddress)}.
     * </p>
     *
     * @param port     the provided local port
     * @param backlog  the provided maximum length of the queue of incoming
     *                 connections
     * @param bindAddr the provided local {@code InetAddress} (can be
     *                 {@code null})
     * @return a new {@code ServerSocket} bound to the provided local port and
     * the provided local {@code InetAddress} with the provided maximum length
     * of the queue of incoming connections
     * @throws IOException if an I/O error occurs in opening the
     *                     {@code ServerSocket}
     */
    public abstract ServerSocket newServerSocket(
            final int port,
            final int backlog,
            final InetAddress bindAddr) throws IOException;

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
