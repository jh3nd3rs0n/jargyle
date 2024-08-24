package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A method selection message from the server.
 */
public final class ServerMethodSelectionMessage {

    /**
     * The {@code Version} for this {@code ServerMethodSelectionMessage}.
     */
    private final Version version;

    /**
     * The {@code Method} for this {@code ServerMethodSelectionMessage}.
     */
    private final Method method;

    /**
     * Constructs a {@code ServerMethodSelectionMessage} with the provided
     * {@code Method}.
     *
     * @param meth the provided {@code Method}
     */
    private ServerMethodSelectionMessage(final Method meth) {
        this.version = Version.V5;
        this.method = meth;
    }

    /**
     * Returns a new {@code ServerMethodSelectionMessage} with the provided
     * {@code Method}.
     *
     * @param meth the provided {@code Method}
     * @return a new {@code ServerMethodSelectionMessage} with the provided
     * {@code Method}
     */
    public static ServerMethodSelectionMessage newInstance(final Method meth) {
        return new ServerMethodSelectionMessage(Objects.requireNonNull(meth));
    }

    /**
     * Returns a new {@code ServerMethodSelectionMessage} from the provided
     * {@code InputStream}. An {@code EOFException} is thrown if the end of
     * the provided {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code ServerMethodSelectionMessage}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static ServerMethodSelectionMessage newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        Method meth = MethodIoHelper.readMethodFrom(in);
        return newInstance(meth);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        ServerMethodSelectionMessage other = (ServerMethodSelectionMessage) obj;
        return this.method.equals(other.method);
    }

    /**
     * Returns the {@code Method} of this
     * {@code ServerMethodSelectionMessage}.
     *
     * @return the {@code Method} of this
     * {@code ServerMethodSelectionMessage}
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Returns the {@code Version} of this
     * {@code ServerMethodSelectionMessage}.
     *
     * @return the {@code Version} of this
     * {@code ServerMethodSelectionMessage}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.version.hashCode();
        result = prime * result + this.method.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this
     * {@code ServerMethodSelectionMessage}.
     *
     * @return the {@code byte} array of this
     * {@code ServerMethodSelectionMessage}
     */
    public byte[] toByteArray() {
        return new byte[]{this.version.byteValue(), this.method.byteValue()};
    }

    /**
     * Returns the {@code String} representation of this
     * {@code ServerMethodSelectionMessage}.
     *
     * @return the {@code String} representation of this
     * {@code ServerMethodSelectionMessage}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", method=" +
                this.method +
                "]";
    }

}
