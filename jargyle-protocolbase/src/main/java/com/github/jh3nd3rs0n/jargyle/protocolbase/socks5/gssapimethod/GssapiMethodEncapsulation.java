package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.MessageProp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * A {@code MethodEncapsulation} that provides networking objects that use
 * GSS-API to encapsulate data to be sent and use GSS-API to de-encapsulate
 * data received.
 */
public final class GssapiMethodEncapsulation extends MethodEncapsulation {

    /**
     * The {@code GSSContext} to be used in encapsulating and
     * de-encapsulating data.
     */
    private final GSSContext gssContext;

    /**
     * The {@code MessageProp} to be used per-message.
     */
    private final MessageProp messageProp;

    /**
     * The {@code Socket} that uses GSS-API to encapsulate data to be sent
     * and to de-encapsulate data received.
     */
    private final Socket socket;

    /**
     * Constructs a {@code GssapiMethodEncapsulation} with the provided
     * {@code Socket}, the provided {@code GSSContext} to be used in
     * encapsulating and de-encapsulating data, and the provided
     * {@code MessageProp} to be used per-message.
     *
     * @param sock    the provided {@code Socket}
     * @param context the provided {@code GSSContext} to be used in
     *                encapsulating and de-encapsulating data
     * @param prop    the provided {@code MessageProp} to be used per-message
     *                (can be {@code null})
     */
    public GssapiMethodEncapsulation(
            final Socket sock,
            final GSSContext context,
            final MessageProp prop) {
        this.gssContext = Objects.requireNonNull(context);
        this.messageProp = prop;
        this.socket = new GssSocket(
                Objects.requireNonNull(sock),
                this.gssContext,
                this.messageProp);
    }

    @Override
    public DatagramSocket getDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException {
        return new GssDatagramSocket(
                datagramSocket, this.gssContext, this.messageProp);
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [socket=" +
                this.socket +
                "]";
    }

}
