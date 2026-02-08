package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * A factory that creates {@code ServerSocket}s.
 */
public abstract class ServerSocketFactory {

    /**
     * Allows the construction of subclass instances.
     */
    public ServerSocketFactory() { }

    /**
     * Returns the default instance of {@code ServerSocketFactory}. The 
     * default instance creates plain {@code ServerSocket}s.
     *
     * @return the default instance of {@code ServerSocketFactory}
     */    
    public static ServerSocketFactory getDefault() {
        return DefaultServerSocketFactory.getInstance();
    }

    /**
     * Returns a {@code ServerSocketFactory}. If the system property
     * {@code socksClient.socksServerUri} is set and the value is valid, a new
     * {@code SocksServerSocketFactory} is returned. Otherwise, the default
     * instance of {@code ServerSocketFactory} from {@link #getDefault()} is
     * returned.
     *
     * @return a {@code ServerSocketFactory}
     */    
    public static ServerSocketFactory getInstance() {
        SocksServerSocketFactory socksServerSocketFactory =
                SocksServerSocketFactory.newInstance();
        if (socksServerSocketFactory != null) {
            return socksServerSocketFactory;
        }
        return getDefault();
    }

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

}
