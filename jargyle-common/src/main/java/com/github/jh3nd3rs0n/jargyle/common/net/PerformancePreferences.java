package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.Digit;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * Performance preferences for a TCP socket described by three digits whose
 * values indicate the relative importance of short connection time, low
 * latency, and high bandwidth.
 */
@ValuesValueTypeDoc(
        description = "Performance preferences for a TCP socket described " +
                "by three digits whose values indicate the relative " +
                "importance of short connection time, low latency, and high " +
                "bandwidth",
        elementValueType = Digit.class,
        name = "Performance Preferences",
        syntax = "DIGITDIGITDIGIT",
        syntaxName = "PERFORMANCE_PREFERENCES"
)
public final class PerformancePreferences {

    /**
     * The regular expression for performance preferences.
     */
    private static final String REGEX = "\\A\\d\\d\\d\\z";

    /**
     * The {@code Digit} of relative importance for high
     * bandwidth.
     */
    private final Digit bandwidthImportance;

    /**
     * The {@code Digit} of relative importance for short
     * connection time.
     */
    private final Digit connectionTimeImportance;

    /**
     * The {@code Digit} of relative importance for low latency.
     */
    private final Digit latencyImportance;

    /**
     * Constructs a {@code PerformancePreferences} of the provided relative
     * importance of short connection time, the provided relative importance
     * of low latency, and the provided relative importance of high bandwidth.
     *
     * @param connectionTime the provided relative importance of short
     *                       connection time
     * @param latency        the provided relative importance of low latency
     * @param bandwidth      the provided relative importance of high bandwidth
     */
    private PerformancePreferences(
            final Digit connectionTime,
            final Digit latency,
            final Digit bandwidth) {
        this.connectionTimeImportance = Objects.requireNonNull(connectionTime);
        this.latencyImportance = Objects.requireNonNull(latency);
        this.bandwidthImportance = Objects.requireNonNull(bandwidth);
    }

    /**
     * Constructs a {@code PerformancePreferences} of another
     * {@code PerformancePreferences}.
     *
     * @param other the other {@code PerformancePreferences}
     */
    private PerformancePreferences(final PerformancePreferences other) {
        this.connectionTimeImportance = other.connectionTimeImportance;
        this.latencyImportance = other.latencyImportance;
        this.bandwidthImportance = other.bandwidthImportance;
    }

    /**
     * Returns a {@code PerformancePreferences} of the provided relative
     * importance of short connection time, the provided relative importance
     * of low latency, and the provided relative importance of high bandwidth.
     *
     * @param connectionTime the provided relative importance of short
     *                       connection time
     * @param latency        the provided relative importance of low latency
     * @param bandwidth      the provided relative importance of high bandwidth
     * @return a {@code PerformancePreferences} of the provided relative
     * importance of short connection time, the provided relative importance
     * of low latency, and the provided relative importance of high bandwidth
     */
    public static PerformancePreferences of(
            final Digit connectionTime,
            final Digit latency,
            final Digit bandwidth) {
        return new PerformancePreferences(connectionTime, latency, bandwidth);
    }

    /**
     * Returns a {@code PerformancePreferences} of another
     * {@code PerformancePreferences}.
     *
     * @param other the other {@code PerformancePreferences}
     * @return a {@code PerformancePreferences} of another
     * {@code PerformancePreferences}
     */
    public static PerformancePreferences of(
            final PerformancePreferences other) {
        return new PerformancePreferences(other);
    }

    /**
     * Returns a new {@code PerformancePreferences} from the provided
     * {@code String} of three digits whose values indicate the relative
     * importance of short connection time, low latency, and high bandwidth.
     * An {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is not valid.
     *
     * @param s the provided {@code String} of three digits
     * @return a new {@code PerformancePreferences} from the provided
     * {@code String} of three digits whose values indicate the relative
     * importance of short connection time, low latency, and high bandwidth
     */
    public static PerformancePreferences newInstanceFrom(final String s) {
        if (!s.matches(REGEX)) {
            throw new IllegalArgumentException(
                    "must be a string of 3 digits");
        }
        char[] sChars = s.toCharArray();
        Digit connectionTime = Digit.valueOf(Character.toString(sChars[0]));
        Digit latency = Digit.valueOf(Character.toString(sChars[1]));
        Digit bandwidth = Digit.valueOf(Character.toString(sChars[2]));
        return new PerformancePreferences(connectionTime, latency, bandwidth);
    }

    /**
     * Applies this {@code PerformancePreferences} to the provided
     * {@code ServerSocket}.
     *
     * @param serverSocket the provided {@code ServerSocket}
     */
    public void applyTo(final ServerSocket serverSocket) {
        serverSocket.setPerformancePreferences(
                this.connectionTimeImportance.intValue(),
                this.latencyImportance.intValue(),
                this.bandwidthImportance.intValue());
    }

    /**
     * Applies this {@code PerformancePreferences} to the provided
     * {@code Socket}.
     *
     * @param socket the provided {@code Socket}
     */
    public void applyTo(final Socket socket) {
        socket.setPerformancePreferences(
                this.connectionTimeImportance.intValue(),
                this.latencyImportance.intValue(),
                this.bandwidthImportance.intValue());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PerformancePreferences other = (PerformancePreferences) obj;
        if (!this.bandwidthImportance.equals(other.bandwidthImportance)) {
            return false;
        }
        if (!this.connectionTimeImportance.equals(
                other.connectionTimeImportance)) {
            return false;
        }
        return this.latencyImportance.equals(other.latencyImportance);
    }

    /**
     * Returns the relative importance of high bandwidth.
     *
     * @return the relative importance of high bandwidth
     */
    public Digit getBandwidthImportance() {
        return this.bandwidthImportance;
    }

    /**
     * Returns the relative importance of short connection time.
     *
     * @return the relative importance of short connection time
     */
    public Digit getConnectionTimeImportance() {
        return this.connectionTimeImportance;
    }

    /**
     * Returns the relative importance of low latency.
     *
     * @return the relative importance of low latency
     */
    public Digit getLatencyImportance() {
        return this.latencyImportance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.bandwidthImportance.hashCode();
        result = prime * result + this.connectionTimeImportance.hashCode();
        result = prime * result + this.latencyImportance.hashCode();
        return result;
    }

    /**
     * Returns the {@code String} representation of this
     * {@code PerformancePreferences}. The {@code String} representation
     * is of three digits whose values indicate the relative importance of
     * short connection time, low latency, and high bandwidth.
     *
     * @return the {@code String} representation of this
     * {@code PerformancePreferences}
     */
    @Override
    public String toString() {
        return this.connectionTimeImportance.toString() +
                this.latencyImportance.toString() +
                this.bandwidthImportance.toString();
    }

}
