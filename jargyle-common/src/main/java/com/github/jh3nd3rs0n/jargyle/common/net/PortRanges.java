package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@code PortRange}s.
 */
@ValuesValueTypeDoc(
        description = "A comma separated list of port ranges.",
        elementValueType = PortRange.class,
        name = "Port Ranges",
        syntax = "[PORT_RANGE1[,PORT_RANGE2[...]]]",
        syntaxName = "PORT_RANGES"
)
public final class PortRanges {

    /**
     * The default {@code PortRanges} (0).
     */
    private static final PortRanges DEFAULT_INSTANCE = PortRanges.of(
            PortRange.getDefault());

    /**
     * The {@code List} of {@code PortRange}s.
     */
    private final List<PortRange> portRanges;

    /**
     * Constructs a {@code PortRanges} with the provided {@code List} of
     * {@code PortRange}s.
     *
     * @param prtRanges the provided {@code List} of {@code PortRange}s
     */
    private PortRanges(final List<PortRange> prtRanges) {
        this.portRanges = new ArrayList<>(prtRanges);
    }

    /**
     * Returns the default {@code PortRanges}.
     *
     * @return the default {@code PortRanges}
     */
    public static PortRanges getDefault() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a {@code PortRanges} of the provided {@code List} of
     * {@code PortRange}s.
     *
     * @param prtRanges the provided {@code List} of {@code PortRange}s
     * @return a {@code PortRanges} of the provided {@code List} of
     * {@code PortRange}s
     */
    public static PortRanges of(final List<PortRange> prtRanges) {
        return new PortRanges(prtRanges);
    }

    /**
     * Returns a {@code PortRanges} of the provided {@code PortRange}s.
     *
     * @param prtRanges the provided {@code PortRange}s
     * @return a {@code PortRanges} of the provided {@code PortRange}s
     */
    public static PortRanges of(final PortRange... prtRanges) {
        return of(Arrays.asList(prtRanges));
    }

    /**
     * Returns a new {@code PortRanges} from the provided {@code String}. The
     * provided {@code String} must be a comma separated list of
     * {@code String} representations of {@code PortRange}s. The provided
     * {@code String} can also be empty which would result in an empty
     * {@code PortRanges}. An {@code IllegalArgumentException} is thrown if
     * the provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new {@code PortRanges} from the provided {@code String}
     */
    public static PortRanges newInstanceFrom(final String s) {
        List<PortRange> prtRanges = new ArrayList<>();
        if (s.isEmpty()) {
            return of(prtRanges);
        }
        String[] sElements = s.split(",");
        for (String sElement : sElements) {
            PortRange prtRange = PortRange.newInstanceFrom(sElement);
            prtRanges.add(prtRange);
        }
        return of(prtRanges);
    }

    /**
     * Returns a {@code boolean} value to indicate if this {@code PortRanges}
     * has any {@code PortRange} that covers the provided {@code Port} value.
     *
     * @param port the provided {@code Port} value
     * @return a {@code boolean} value to indicate if this {@code PortRanges}
     * has any {@code PortRange} that covers the provided {@code Port} value
     */
    public boolean anyCovers(final Port port) {
        for (PortRange portRange : this.portRanges) {
            if (portRange.covers(port)) {
                return true;
            }
        }
        return false;
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
        PortRanges other = (PortRanges) obj;
        return this.portRanges.equals(other.portRanges);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.portRanges.hashCode();
        return result;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code PortRange}s.
     *
     * @return an unmodifiable {@code List} of {@code PortRange}s
     */
    public List<PortRange> toList() {
        return Collections.unmodifiableList(this.portRanges);
    }

    /**
     * Returns the {@code String} representation of this {@code PortRanges}.
     * The {@code String} representation is a comma separated list of
     * {@code String} representations of {@code PortRange}s. If this
     * {@code PortRanges} has no {@code PortRange}s, then the {@code String}
     * representation of this {@code String} would be an empty {@code String}.
     *
     * @return the {@code String} representation of this {@code PortRanges}
     */
    @Override
    public String toString() {
        return this.portRanges.stream()
                .map(PortRange::toString)
                .collect(Collectors.joining(","));
    }

}
