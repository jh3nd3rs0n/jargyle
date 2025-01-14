package com.github.jh3nd3rs0n.jargyle.common.bytes;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.util.Arrays;
import java.util.Base64;

/**
 * A collection of {@code byte}s.
 */
@SingleValueTypeDoc(
        description = "A Base64 string representing a series of bytes",
        name = "Bytes",
        syntax = "BASE_64_STRING",
        syntaxName = "BYTES"
)
public final class Bytes {

    /**
     * The {@code byte} array of this {@code Bytes}.
     */
    private final byte[] bytes;

    /**
     * Constructs a {@code Bytes} with the provided {@code byte} array.
     *
     * @param b the provided {@code byte} array
     */
    private Bytes(final byte[] b) {
        this.bytes = Arrays.copyOf(b, b.length);
    }

    /**
     * Returns a new {@code Bytes} from the provided {@code String} in Base64.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not in valid Base64.
     *
     * @param s the provided {@code String} in Base64
     * @return a new {@code Bytes} from the provided {@code String} in Base64
     */
    public static Bytes newInstanceFrom(final String s) {
        return of(Base64.getDecoder().decode(s));
    }

    /**
     * Returns a new {@code Bytes} of the provided {@code byte} array.
     *
     * @param b the provided {@code byte} array
     * @return a new {@code Bytes} of the provided {@code byte} array
     */
    public static Bytes of(final byte[] b) {
        return new Bytes(b);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Bytes other = (Bytes) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.bytes);
        return result;
    }

    /**
     * Returns the {@code byte} array of this {@code Bytes}.
     *
     * @return the {@code byte} array of this {@code Bytes}
     */
    public byte[] toArray() {
        return Arrays.copyOf(this.bytes, this.bytes.length);
    }

    /**
     * Returns the {@code String} representation of this {@code Bytes}. The
     * {@code String} representation consists of the {@code byte} array of
     * this {@code Bytes} in Base64
     *
     * @return the {@code String} representation of this {@code Bytes}
     */
    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(this.bytes);
    }

}
