package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.net.Socket;

/**
 * Performance preferences for a {@code Socket}.
 * <p>
 * From {@link java.net.Socket#setPerformancePreferences(int, int, int)}:
 * </p>
 * <p>
 * Performance preferences are described by three integers whose values
 * indicate the relative importance of short connection time, low latency,
 * and high bandwidth. The absolute values of the integers are irrelevant; in
 * order to choose a protocol the values are simply compared, with larger
 * values indicating stronger preferences. Negative values represent a lower
 * priority than positive values. If the application prefers short connection
 * time over both low latency and high bandwidth, for example, a
 * {@code PerformancePreferences} with the values {@code (1, 0, 0)} could be
 * applied to the {@code Socket}. If the application prefers high bandwidth
 * above low latency, and low latency above short connection time, then a
 * {@code PerformancePreferences} with the values {@code (0, 1, 2)} could be
 * applied to the {@code Socket}.
 * </p>
 */
public final class PerformancePreferences {

    /**
     * The {@code int} value expressing the relative importance of a short
     * connection time.
     */
    private final int shortConnectionTimeImportance;

    /**
     * The {@code int} value expressing the relative importance of low latency.
     */
    private final int lowLatencyImportance;

    /**
     * The {@code int} value expressing the relative importance of high
     * bandwidth.
     */
    private final int highBandwidthImportance;

    /**
     * Constructs a {@code PerformancePreferences} with an {@code int}
     * expressing the relative importance of a short connection time, an
     * {@code int} expressing the relative importance of low latency, and an
     * {@code int} expressing the relative importance of high bandwidth.
     *
     * @param connectionTime an {@code int} expressing the relative
     *                       importance of a short connection time
     * @param latency        an {@code int} expressing the relative
     *                       importance of low latency
     * @param bandwidth      an {@code int} expressing the relative
     *                       importance of high bandwidth
     */
    public PerformancePreferences(
            final int connectionTime, final int latency, final int bandwidth) {
        this.shortConnectionTimeImportance = connectionTime;
        this.lowLatencyImportance = latency;
        this.highBandwidthImportance = bandwidth;
    }

    /**
     * Applies this {@code PerformancePreferences} to the provided
     * {@code Socket}.
     *
     * @param socket the provided {@code Socket}
     */
    public void applyTo(final Socket socket) {
        socket.setPerformancePreferences(
                this.shortConnectionTimeImportance,
                this.lowLatencyImportance,
                this.highBandwidthImportance);
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
        PerformancePreferences other = (PerformancePreferences) obj;
        if (this.shortConnectionTimeImportance != other.shortConnectionTimeImportance) {
            return false;
        }
        if (this.lowLatencyImportance != other.lowLatencyImportance) {
            return false;
        }
        return this.highBandwidthImportance == other.highBandwidthImportance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.shortConnectionTimeImportance;
        result = prime * result + this.lowLatencyImportance;
        result = prime * result + this.highBandwidthImportance;
        return result;
    }

}
