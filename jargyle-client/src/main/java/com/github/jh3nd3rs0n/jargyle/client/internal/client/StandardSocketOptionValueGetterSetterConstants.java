package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.net.*;

/**
 * Standard {@code SocketOptionValueGetterSetter} constants.
 */
public final class StandardSocketOptionValueGetterSetterConstants {


    /**
     * {@code SocketOptionValueGetterSetter} constant for
     * {@link StandardSocketOptions#SO_RCVBUF}.
     */
    public static final SocketOptionValueGetterSetter<Integer> SO_RCVBUF = new SocketOptionValueGetterSetter<>(StandardSocketOptions.SO_RCVBUF) {

        @Override
        public Integer getSocketOptionValue(
                final ServerSocket serverSocket) throws SocketException {
            return serverSocket.getReceiveBufferSize();
        }

        @Override
        public ServerSocket setSocketOptionValue(
                final Integer value,
                final ServerSocket serverSocket) throws SocketException {
            serverSocket.setReceiveBufferSize(value);
            return serverSocket;
        }

    };

    /**
     * {@code SocketOptionValueGetterSetter} constant for
     * {@link StandardSocketOptions#SO_REUSEADDR}.
     */
    public static final SocketOptionValueGetterSetter<Boolean> SO_REUSEADDR = new SocketOptionValueGetterSetter<>(StandardSocketOptions.SO_REUSEADDR) {

        @Override
        public Boolean getSocketOptionValue(
                final ServerSocket serverSocket) throws SocketException {
            return serverSocket.getReuseAddress();
        }

        @Override
        public ServerSocket setSocketOptionValue(
                final Boolean value,
                final ServerSocket serverSocket) throws SocketException {
            serverSocket.setReuseAddress(value);
            return serverSocket;
        }

    };

    /**
     * Prevents the construction of unnecessary instances.
     */
    private StandardSocketOptionValueGetterSetterConstants() { }
    
}
