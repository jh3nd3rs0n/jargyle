package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.internal.net.FilterSocket;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import java.io.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * A {@code Socket} that uses GSS-API to encapsulate data to be sent and to
 * de-encapsulate data received.
 */
final class GssSocket extends FilterSocket {

    /**
     * The {@code GSSContext} used in encapsulating and de-encapsulating data.
     */
    private final GSSContext gssContext;

    /**
     * The {@code MessageProp} used per-message.
     */
    private final MessageProp messageProp;

    /**
     * The {@code InputStream} of this {@code GssSocket}.
     */
    private InputStream inputStream;

    /**
     * The {@code OutputStream} of this {@code GssSocket}.
     */
    private OutputStream outputStream;

    /**
     * Constructs a {@code GssSocket} with the provided underlying
     * {@code Socket}, the provided {@code GSSContext} to be used in
     * encapsulating and de-encapsulating data, and the provided
     * {@code MessageProp} to be used per-message.
     *
     * @param sock    the provided underlying {@code Socket}
     * @param context the provided {@code GSSContext} to be used in
     *                encapsulating and de-encapsulating data
     * @param prop    the provided {@code MessageProp} to be used per-message
     *                (can be {@code null})
     */
    public GssSocket(
            final Socket sock,
            final GSSContext context,
            final MessageProp prop) {
        super(sock);
        MessageProp prp = null;
        if (prop != null) {
            prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
        }
        this.gssContext = Objects.requireNonNull(context);
        this.messageProp = prp;
        this.inputStream = null;
        this.outputStream = null;
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            this.gssContext.dispose();
        } catch (GSSException e) {
            throw new IOException(e);
        }
        this.socket.close();
    }

    @Override
    public SocketChannel getChannel() {
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

    @Override
    public InputStream getInputStream() throws IOException {
        if (this.inputStream != null) {
            return this.inputStream;
        }
        InputStream inStream = this.socket.getInputStream();
        if (this.messageProp != null) {
            this.inputStream = new GssUnwrappedInputStream(
                    this.gssContext, inStream);
        } else {
            this.inputStream = inStream;
        }
        return this.inputStream;
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
    public OutputStream getOutputStream() throws IOException {
        if (this.outputStream != null) {
            return this.outputStream;
        }
        OutputStream outStream = this.socket.getOutputStream();
        if (this.messageProp != null) {
            this.outputStream = new GssWrappedOutputStream(
                    this.gssContext, this.messageProp, outStream);
        } else {
            this.outputStream = outStream;
        }
        return this.outputStream;
    }

    /**
     * Returns the {@code String} representation of this {@code GssSocket}.
     *
     * @return the {@code String} representation of this {@code GssSocket}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [gssContext=" +
                this.gssContext +
                ", messageProp=" +
                this.messageProp +
                ", socket=" +
                this.socket +
                "]";
    }

    /**
     * A {@code InputStream} that uses GSS-API to de-encapsulate data received.
     */
    private static final class GssUnwrappedInputStream extends InputStream {

        /**
         * The {@code GSSContext} used to de-encapsulate data.
         */
        private final GSSContext gssContext;

        /**
         * The underlying {@code InputStream}.
         */
        private final InputStream in;

        /**
         * The buffer {@code InputStream} of de-encapsulated data.
         */
        private ByteArrayInputStream bufferIn;

        /**
         * The {@code boolean} value to indicate if this
         * {@code GssUnwrappedInputStream}.
         */
        private boolean closed;

        /**
         * Constructs a {@code GssUnwrappedInputStream} with the provided
         * {@code GSSContext} to be used in de-encapsulating data and the
         * provided underlying {@code InputStream}.
         *
         * @param context  the provided {@code GSSContext} to be used in
         *                 de-encapsulating data
         * @param inStream the provided underlying {@code InputStream}
         */
        public GssUnwrappedInputStream(
                final GSSContext context,
                final InputStream inStream) {
            this.bufferIn = new ByteArrayInputStream(new byte[]{});
            this.closed = false;
            this.gssContext = Objects.requireNonNull(context);
            this.in = Objects.requireNonNull(inStream);
        }

        @Override
        public int available() throws IOException {
            if (this.closed) {
                throw new IOException("stream closed");
            }
            int bufferInAvailable = this.bufferIn.available();
            if (bufferInAvailable > 0) {
                return bufferInAvailable;
            }
            return this.in.available();
        }

        @Override
        public void close() throws IOException {
            this.bufferIn = new ByteArrayInputStream(new byte[]{});
            this.in.close();
            this.closed = true;
        }

        @Override
        public int read() throws IOException {
            if (this.closed) {
                return -1;
            }
            if (this.bufferIn.available() == 0) {
                int b = this.in.read();
                if (b == -1) {
                    this.closed = true;
                    return b;
                }
                EncapsulatedUserDataMessage message =
                        EncapsulatedUserDataMessage.newInstanceFrom(
                                new SequenceInputStream(
                                        new ByteArrayInputStream(
                                                new byte[]{(byte) b}),
                                        this.in));
                byte[] token = message.getToken().getBytes();
                MessageProp prop = new MessageProp(0, false);
                try {
                    token = this.gssContext.unwrap(
                            token, 0, token.length, prop);
                } catch (GSSException e) {
                    throw new IOException(e);
                }
                this.bufferIn = new ByteArrayInputStream(token);
            }
            return this.bufferIn.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return this.read(b, 0, b.length);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            Objects.requireNonNull(b);
            if (off < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "offset is negative: %s",
                        off));
            }
            if (len < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "specified length is negative: %s",
                        len));
            }
            if (len > b.length - off) {
                throw new IndexOutOfBoundsException(String.format(
                        "specified length is greater than "
                                + "the length of the array minus offset: "
                                + "len (%s) > length of array (%s) - off (%s)",
                        len,
                        b.length,
                        off));
            }
            if (this.closed) {
                return -1;
            }
            int offset = off;
            int bufferInAvailable = this.bufferIn.available();
            if (bufferInAvailable == 0) {
                int by = this.read();
                if (by == -1) {
                    return by;
                }
                b[offset] = (byte) by;
                offset++;
                bufferInAvailable = 1 + this.bufferIn.available();
            }
            int length = len;
            if (length > bufferInAvailable) {
                length = bufferInAvailable;
            }
            for (int i = offset; i < off + length; i++) {
                b[i] = (byte) this.bufferIn.read();
            }
            return length;
        }

    }

    /**
     * A {@code OutputStream} that uses GSS-API to encapsulate data to be sent.
     */
    private static final class GssWrappedOutputStream extends OutputStream {

        /**
         * The {@code GSSContext} used to encapsulate data.
         */
        private final GSSContext gssContext;

        /**
         * The {@code MessageProp} used per-message.
         */
        private final MessageProp messageProp;

        /**
         * The underlying {@code OutputStream}.
         */
        private final OutputStream out;

        /**
         * The limit of the message to be encapsulated.
         */
        private final int wrapSizeLimit;

        /**
         * The buffer {@code OutputStream} of encapsulated data.
         */
        private ByteArrayOutputStream bufferOut;

        /**
         * The length of the buffer {@code OutputStream}.
         */
        private int bufferOutLength;

        /**
         * Constructs a {@code GssUnwrappedOutputStream} with the provided
         * {@code GSSContext} to be used in encapsulating data, the provided
         * {@code MessageProp} to be used per-message, and the provided
         * underlying {@code OutputStream}.
         *
         * @param context   the provided {@code GSSContext} to be used in
         *                  encapsulating data
         * @param prop      the provided {@code MessageProp} to used per-message
         * @param outStream the provided underlying {@code OutputStream}
         */
        public GssWrappedOutputStream(
                final GSSContext context,
                final MessageProp prop,
                final OutputStream outStream) {
            int sizeLimit;
            try {
                sizeLimit = context.getWrapSizeLimit(
                        prop.getQOP(),
                        prop.getPrivacy(),
                        Token.MAX_LENGTH);
            } catch (GSSException e) {
                throw new AssertionError(e);
            }
            this.bufferOut = new ByteArrayOutputStream();
            this.bufferOutLength = 0;
            this.gssContext = Objects.requireNonNull(context);
            this.messageProp = new MessageProp(
                    prop.getQOP(), prop.getPrivacy());
            this.out = Objects.requireNonNull(outStream);
            this.wrapSizeLimit = sizeLimit;
        }

        @Override
        public synchronized void close() throws IOException {
            this.bufferOut = new ByteArrayOutputStream();
            this.bufferOutLength = 0;
            this.out.close();
        }

        @Override
        public void flush() throws IOException {
            byte[] bytes = this.bufferOut.toByteArray();
            MessageProp prop = new MessageProp(
                    this.messageProp.getQOP(),
                    this.messageProp.getPrivacy());
            byte[] token;
            try {
                token = this.gssContext.wrap(
                        bytes, 0, bytes.length, prop);
            } catch (GSSException e) {
                throw new IOException(e);
            }
            this.out.write(EncapsulatedUserDataMessage.newInstance(
                    Token.newInstance(token)).toByteArray());
            this.out.flush();
            this.bufferOut = new ByteArrayOutputStream();
            this.bufferOutLength = 0;
        }

        @Override
        public void write(byte[] b) throws IOException {
            this.write(b, 0, b.length);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            Objects.requireNonNull(b);
            if (off < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "offset is negative: %s",
                        off));
            }
            if (len < 0) {
                throw new IndexOutOfBoundsException(String.format(
                        "specified length is negative: %s",
                        len));
            }
            if (off + len > b.length) {
                throw new IndexOutOfBoundsException(String.format(
                        "offset and specified length is greater than "
                                + "the length of the array: "
                                + "off (%s) + len (%s) > length of array (%s)",
                        off,
                        len,
                        b.length));
            }
            for (int i = off; i < off + len; i++) {
                this.write(b[i]);
            }
        }

        @Override
        public void write(int b) throws IOException {
            this.bufferOut.write(b);
            if (++this.bufferOutLength == this.wrapSizeLimit) {
                this.flush();
            }
        }

    }

}
