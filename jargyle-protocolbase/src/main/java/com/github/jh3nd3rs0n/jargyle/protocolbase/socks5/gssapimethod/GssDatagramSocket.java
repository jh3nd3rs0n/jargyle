package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.internal.net.FilterDatagramSocket;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

/**
 * A {@code DatagramSocket} that uses GSS-API to encapsulate data to be sent
 * and to de-encapsulate data received.
 */
final class GssDatagramSocket extends FilterDatagramSocket {

    /**
     * The {@code GSSContext} used in encapsulating and de-encapsulating data.
     */
    private final GSSContext gssContext;

    /**
     * The {@code MessageProp} used per-message.
     */
    private final MessageProp messageProp;

    /**
     * The limit of the message to be encapsulated.
     */
    private final int wrapSizeLimit;

    /**
     * Constructs a {@code GssDatagramSocket} with the provided underlying
     * {@code DatagramSocket}, the provided {@code GSSContext} to be used in
     * encapsulating and de-encapsulating data, and the provided
     * {@code MessageProp} to be used per-message.
     *
     * @param datagramSock the provided underlying {@code DatagramSocket}
     * @param context      the provided {@code GSSContext} to be used in
     *                     encapsulating and de-encapsulating data
     * @param prop         the provided {@code MessageProp} to be used per-message
     *                     (can be {@code null})
     * @throws SocketException if the {@code GssDatagramSocket} can not be
     *                         opened, or the {@code GssDatagramSocket} could
     *                         not bind to the specified local port (which in
     *                         either case will not happen since the
     *                         {@code GssDatagramSocket} is neither opened nor
     *                         bound)
     */
    public GssDatagramSocket(
            final DatagramSocket datagramSock,
            final GSSContext context,
            final MessageProp prop) throws SocketException {
        super(datagramSock);
        MessageProp prp = null;
        int sizeLimit = Token.MAX_LENGTH;
        if (prop != null) {
            prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
            try {
                sizeLimit = context.getWrapSizeLimit(
                        prop.getQOP(),
                        prop.getPrivacy(),
                        Token.MAX_LENGTH);
            } catch (GSSException e) {
                throw new AssertionError(e);
            }
        }
        this.gssContext = context;
        this.messageProp = prp;
        this.wrapSizeLimit = sizeLimit;
    }

    /**
     * Closes this {@code GssDatagramSocket}. An {@code UncheckedIOException}
     * is thrown if the {@code GSSContext} used in encapsulating and
     * de-encapsulating data is unable to dispose properly.
     */
    @Override
    public void close() {
        try {
            this.gssContext.dispose();
        } catch (GSSException e) {
            throw new UncheckedIOException(new IOException(e));
        }
        super.close();
    }

    /**
     * Throws an {@code UnsupportedOperationException}.
     *
     * @return {@code null}
     */
    @Override
    public DatagramChannel getChannel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@code GSSContext} used in encapsulating and
     * de-encapsulating data.
     *
     * @return the {@code GSSContext} used in encapsulating and
     * de-encapsulating data
     */
    public GSSContext getGSSContext() {
        return this.gssContext;
    }

    /**
     * Returns the {@code MessageProp} used per-message.
     *
     * @return the {@code MessageProp} used per-message
     */
    public MessageProp getMessageProp() {
        if (this.messageProp != null) {
            return new MessageProp(
                    this.messageProp.getQOP(), this.messageProp.getPrivacy());
        }
        return null;
    }

    @Override
    public synchronized void receive(DatagramPacket p) throws IOException {
        super.receive(p);
        if (this.messageProp != null) {
            byte[] data = Arrays.copyOfRange(
                    p.getData(), p.getOffset(), p.getLength());
            EncapsulatedUserDataMessage message =
                    EncapsulatedUserDataMessage.newInstanceFrom(data);
            byte[] token = message.getToken().getBytes();
            MessageProp prop = new MessageProp(0, false);
            try {
                token = this.gssContext.unwrap(token, 0, token.length, prop);
            } catch (GSSException e) {
                throw new IOException(e);
            }
            int length = Math.min(token.length, data.length);
            p.setData(token, 0, length);
            p.setLength(length);
        }
    }

    @Override
    public void send(DatagramPacket p) throws IOException {
        if (this.messageProp != null) {
            byte[] data = Arrays.copyOfRange(
                    p.getData(), p.getOffset(), p.getLength());
            if (data.length > this.wrapSizeLimit) {
                throw new IOException(
                        "A message sent on a GssDatagramSocket was larger "
                                + "than the wrap size limit");
            }
            byte[] token;
            MessageProp prop = new MessageProp(
                    this.messageProp.getQOP(),
                    this.messageProp.getPrivacy());
            try {
                token = this.gssContext.wrap(data, 0, data.length, prop);
            } catch (GSSException e) {
                throw new IOException(e);
            }
            EncapsulatedUserDataMessage message =
                    EncapsulatedUserDataMessage.newInstance(Token.newInstance(
                            token));
            byte[] messageBytes = message.toByteArray();
            p.setData(messageBytes, 0, messageBytes.length);
            p.setLength(messageBytes.length);
        }
        super.send(p);
    }

    /**
     * Returns the {@code String} representation of this
     * {@code GssDatagramSocket}.
     *
     * @return the {@code String} representation of this
     * {@code GssDatagramSocket}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getLocalSocketAddress()=" +
                this.getLocalSocketAddress() +
                ", gssContext=" +
                this.gssContext +
                ", messageProp=" +
                this.messageProp +
                "]";
    }

}
