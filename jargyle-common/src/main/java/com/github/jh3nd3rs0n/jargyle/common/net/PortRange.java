package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A range of {@code Port} values between the provided minimum {@code Port}
 * value and the maximum {@code Port} value (inclusive). A {@code PortRange}
 * can also be one {@code Port} value.
 */
@ValuesValueTypeDoc(
        description = "A range of port numbers between the provided minimum " +
                "port number and the maximum port number (inclusive). A " +
                "port range can also be one port number.",
        elementValueType = Port.class,
        name = "Port Range",
        syntax = "PORT|PORT1-PORT2",
        syntaxName = "PORT_RANGE"
)
public final class PortRange implements Iterable<Port> {

    /**
     * The default {@code PortRange} (0).
     */
    private static final PortRange DEFAULT_INSTANCE = PortRange.newInstance(
            Port.newInstanceOf(0));
    /**
     * The minimum {@code Port} value.
     */
    private final Port minPort;
    /**
     * The maximum {@code Port} value.
     */
    private final Port maxPort;

    /**
     * Constructs a {@code PortRange} with the provided minimum {@code Port}
     * value and the provided maximum {@code Port} value. An
     * {@code IllegalArgumentException} is thrown if the provided minimum
     * {@code Port} value is greater than the maximum {@code Port} value.
     *
     * @param minPrt the provided minimum {@code Port} value
     * @param maxPrt the provided maximum {@code Port} value
     */
    private PortRange(final Port minPrt, final Port maxPrt) {
        if (minPrt.compareTo(maxPrt) > 0) {
            throw new IllegalArgumentException(String.format(
                    "minimum port (%s) must not be greater than maximum port (%s)",
                    minPrt,
                    maxPrt));
        }
        this.minPort = minPrt;
        this.maxPort = maxPrt;
    }

    /**
     * Returns the default {@code PortRange}
     *
     * @return the default {@code PortRange}
     */
    public static PortRange getDefault() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a new {@code PortRange} with the provided {@code Port} value as
     * both the minimum and maximum value.
     *
     * @param prt the provided {@code Port} value
     * @return a new {@code PortRange} with the provided {@code Port} value as
     * both the minimum and maximum value
     */
    public static PortRange newInstance(final Port prt) {
        return newInstance(prt, prt);
    }

    /**
     * Returns a new {@code PortRange} with the provided minimum {@code Port}
     * value and the maximum {@code Port} value. An
     * {@code IllegalArgumentException} is thrown if the provided minimum
     * {@code Port} value is greater than the maximum {@code Port} value.
     *
     * @param minPrt the provided minimum {@code Port} value
     * @param maxPrt the provided maximum {@code Port} value
     * @return a new {@code PortRange} with the provided minimum {@code Port}
     * value and the maximum {@code Port} value
     */
    public static PortRange newInstance(final Port minPrt, final Port maxPrt) {
        return new PortRange(minPrt, maxPrt);
    }

    /**
     * Returns a new {@code PortRange} of the provided {@code String}. The
     * provided {@code String} must be a port number followed by a hyphen
     * followed by another port number equal to or greater than the first port
     * number. An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not valid.
     *
     * @param s the provided {@code String}
     * @return a new {@code PortRange} of the provided {@code String}
     */
    public static PortRange newInstanceOf(final String s) {
        String message = String.format(
                "port range must be either of the following formats: "
                        + "INTEGER_BETWEEN_%1$s_AND_%2$s, "
                        + "INTEGER1_BETWEEN_%1$s_AND_%2$s-INTEGER2_BETWEEN_%1$s_AND_%2$s",
                Port.MIN_INT_VALUE,
                Port.MAX_INT_VALUE);
        String[] sElements = s.split("-");
        if (sElements.length < 1 || sElements.length > 2) {
            throw new IllegalArgumentException(message);
        }
        if (sElements.length == 1) {
            String sElement = sElements[0];
            Port prt;
            try {
                prt = Port.newInstanceOf(sElement);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(message, e);
            }
            return newInstance(prt);
        }
        String sElement0 = sElements[0];
        String sElement1 = sElements[1];
        Port minPrt;
        try {
            minPrt = Port.newInstanceOf(sElement0);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        Port maxPrt;
        try {
            maxPrt = Port.newInstanceOf(sElement1);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message, e);
        }
        return newInstance(minPrt, maxPrt);
    }

    /**
     * Returns a {@code boolean} value to indicate if this {@code PortRange}
     * has the provided {@code Port} value.
     *
     * @param port the provided {@code Port} value
     * @return a {@code boolean} value to indicate if this {@code PortRange}
     * has the provided {@code Port} value
     */
    public boolean has(final Port port) {
        return this.minPort.compareTo(port) <= 0 && this.maxPort.compareTo(port) >= 0;
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
        PortRange other = (PortRange) obj;
        if (!this.maxPort.equals(other.maxPort)) {
            return false;
        }
        return this.minPort.equals(other.minPort);
    }

    /**
     * Returns the maximum {@code Port} value.
     *
     * @return the maximum {@code Port} value
     */
    public Port getMaxPort() {
        return this.maxPort;
    }

    /**
     * Returns the minimum {@code Port} value.
     *
     * @return the minimum {@code Port} value
     */
    public Port getMinPort() {
        return this.minPort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.maxPort.hashCode();
        result = prime * result + this.minPort.hashCode();
        return result;
    }

    @Override
    public Iterator<Port> iterator() {
        PortRange prtRange = this;
        return new Iterator<>() {

            private final int end = prtRange.getMaxPort().intValue();
            private int index = prtRange.getMinPort().intValue() - 1;

            @Override
            public boolean hasNext() {
                return this.index < this.end;
            }

            @Override
            public Port next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return Port.newInstanceOf(++this.index);
            }

        };
    }

    /**
     * Returns the {@code String} representation of this {@code PortRange}.
     * If the minimum {@code Port} value and the maximum {@code Port} value
     * are the same, then the {@code String} representation is the
     * {@code String} representation of the minimum {@code Port} value.
     * If the minimum {@code Port} value and the maximum {@code Port} value
     * are different, the {@code String} representation is the {@code String}
     * representation of the minimum {@code Port} value followed by a hyphen
     * followed by the {@code String} representation of the maximum
     * {@code Port} value.
     *
     * @return the {@code String} representation of this {@code PortRange}
     */
    @Override
    public String toString() {
        if (this.minPort.equals(this.maxPort)) {
            return this.minPort.toString();
        }
        return String.format("%s-%s", this.minPort, this.maxPort);
    }

}
