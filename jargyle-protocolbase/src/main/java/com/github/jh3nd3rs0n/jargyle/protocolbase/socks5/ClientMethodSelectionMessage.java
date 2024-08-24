package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.internal.UnsignedByteIoHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A method selection message sent from the client.
 */
public final class ClientMethodSelectionMessage {

    /**
     * The {@code Version} of this {@code ClientMethodSelectionMessage}.
     */
    private final Version version;

    /**
     * The {@code Methods} of this {@code ClientMethodSelectionMessage}.
     */
    private final Methods methods;

    /**
     * Constructs a {@code ClientMethodSelectionMessage} with the provided
     * {@code Methods}.
     *
     * @param meths the provided {@code Methods}
     */
    private ClientMethodSelectionMessage(final Methods meths) {
        this.version = Version.V5;
        this.methods = meths;
    }

    /**
     * Returns a new {@code ClientMethodSelectionMessage} with the provided
     * {@code Methods}. An {@code IllegalArgumentException} is thrown if the
     * provided {@code Methods} has a size greater than 255.
     *
     * @param meths the provided {@code Methods}
     * @return a new {@code ClientMethodSelectionMessage} with the provided
     * {@code Methods}
     */
    public static ClientMethodSelectionMessage newInstance(
            final Methods meths) {
        List<Method> methsList = meths.toList();
        if (methsList.size() > UnsignedByte.MAX_INT_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "number of methods must be no more than %s",
                    UnsignedByte.MAX_INT_VALUE));
        }
        return new ClientMethodSelectionMessage(meths);
    }

    /**
     * Returns a new {@code ClientMethodSelectionMessage} from the provided
     * {@code InputStream}. An {@code EOFException} is thrown if the end of
     * the provided {@code InputStream} is reached.
     *
     * @param in the provided {@code InputStream}
     * @return a new {@code ClientMethodSelectionMessage}
     * @throws IOException if the end of the provided {@code InputStream}
     *                     is reached ({@code EOFException}) or if an I/O
     *                     error occurs
     */
    public static ClientMethodSelectionMessage newInstanceFrom(
            final InputStream in) throws IOException {
        VersionIoHelper.readVersionFrom(in);
        UnsignedByte methodCount = UnsignedByteIoHelper.readUnsignedByteFrom(
                in);
        List<Method> meths = new ArrayList<>();
        for (int i = 0; i < methodCount.intValue(); i++) {
            Method meth;
            try {
                meth = MethodIoHelper.readMethodFrom(in);
            } catch (Socks5Exception e) {
                continue;
            }
            meths.add(meth);
        }
        return newInstance(Methods.of(meths));
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
        ClientMethodSelectionMessage other = (ClientMethodSelectionMessage) obj;
        return this.methods.equals(other.methods);
    }

    /**
     * Returns the {@code Methods} of this
     * {@code ClientMethodSelectionMessage}.
     *
     * @return the {@code Methods} of this
     * {@code ClientMethodSelectionMessage}
     */
    public Methods getMethods() {
        return this.methods;
    }

    /**
     * Returns the {@code Version} of this
     * {@code ClientMethodSelectionMessage}.
     *
     * @return the {@code Version} of this
     * {@code ClientMethodSelectionMessage}
     */
    public Version getVersion() {
        return this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.version.hashCode();
        result = prime * result + this.methods.hashCode();
        return result;
    }

    /**
     * Returns the {@code byte} array of this
     * {@code ClientMethodSelectionMessage}.
     *
     * @return the {@code byte} array of this
     * {@code ClientMethodSelectionMessage}
     */
    public byte[] toByteArray() {
        List<Method> methodsList = this.methods.toList();
        byte[] arr = new byte[2 + methodsList.size()];
        arr[0] = this.version.byteValue();
        arr[1] = UnsignedByte.valueOf(methodsList.size()).byteValue();
        int i = 1;
        for (Method method : methodsList) {
            arr[++i] = method.byteValue();
        }
        return arr;
    }

    /**
     * Returns the {@code String} representation of this
     * {@code ClientMethodSelectionMessage}.
     *
     * @return the {@code String} representation of this
     * {@code ClientMethodSelectionMessage}
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [version=" +
                this.version +
                ", methods=" +
                this.methods +
                "]";
    }

}
