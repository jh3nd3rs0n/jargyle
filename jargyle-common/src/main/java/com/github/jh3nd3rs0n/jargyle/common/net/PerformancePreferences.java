package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.Digit;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.net.ServerSocket;
import java.net.Socket;

@ValuesValueTypeDoc(
		description = "",
		elementValueType = Digit.class,
		name = "Performance Preferences",
		syntax = "DIGITDIGITDIGIT",
		syntaxName = "PERFORMANCE_PREFERENCES"
)
public final class PerformancePreferences {
	
	private static final String REGEX = "\\A\\d\\d\\d\\z";
	
	public static PerformancePreferences newInstance(
			final Digit connectionTime, 
			final Digit latency, 
			final Digit bandwidth) {
		return new PerformancePreferences(connectionTime, latency, bandwidth);
	}
	
	public static PerformancePreferences newInstance(
			final PerformancePreferences other) {
		return new PerformancePreferences(other);
	}
	
	public static PerformancePreferences newInstance(final String s) {
		if (!s.matches(REGEX)) {
			throw new IllegalArgumentException(
					"must be a string consisting of 3 digits");
		}
		char[] sChars = s.toCharArray();
		Digit connectionTime = Digit.newInstance(Character.toString(sChars[0]));
		Digit latency = Digit.newInstance(Character.toString(sChars[1]));
		Digit bandwidth = Digit.newInstance(Character.toString(sChars[2]));
		return new PerformancePreferences(connectionTime, latency, bandwidth);
	}
	
	private final Digit bandwidthImportance;
	private final Digit connectionTimeImportance;
	private final Digit latencyImportance;
	
	private PerformancePreferences(
			final Digit connectionTime, 
			final Digit latency, 
			final Digit bandwidth) {
		this.connectionTimeImportance = connectionTime;
		this.latencyImportance = latency;
		this.bandwidthImportance = bandwidth;
	}
	
	private PerformancePreferences(final PerformancePreferences other) {
		this.connectionTimeImportance = other.connectionTimeImportance;
		this.latencyImportance = other.latencyImportance;
		this.bandwidthImportance = other.bandwidthImportance;
	}
	
	public void applyTo(final ServerSocket serverSocket) {
		serverSocket.setPerformancePreferences(
				this.connectionTimeImportance.intValue(), 
				this.latencyImportance.intValue(), 
				this.bandwidthImportance.intValue());
	}
	public void applyTo(final Socket socket) {
		socket.setPerformancePreferences(
				this.connectionTimeImportance.intValue(), 
				this.latencyImportance.intValue(), 
				this.bandwidthImportance.intValue());
	}
	
	@Override
	public boolean equals(Object obj) {
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
		if (this.bandwidthImportance == null) {
			if (other.bandwidthImportance != null) {
				return false;
			}
		} else if (!this.bandwidthImportance.equals(
				other.bandwidthImportance)) {
			return false;
		}
		if (this.connectionTimeImportance == null) {
			if (other.connectionTimeImportance != null) {
				return false;
			}
		} else if (!this.connectionTimeImportance.equals(
				other.connectionTimeImportance)) {
			return false;
		}
		if (this.latencyImportance == null) {
			if (other.latencyImportance != null) {
				return false;
			}
		} else if (!this.latencyImportance.equals(other.latencyImportance)) {
			return false;
		}
		return true;
	}

	public Digit getBandwidthImportance() {
		return this.bandwidthImportance;
	}

	public Digit getConnectionTimeImportance() {
		return this.connectionTimeImportance;
	}
	
	public Digit getLatencyImportance() {
		return this.latencyImportance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.bandwidthImportance == null) ? 
				0 : this.bandwidthImportance.hashCode());
		result = prime * result + ((this.connectionTimeImportance == null) ? 
				0 : this.connectionTimeImportance.hashCode());
		result = prime * result + ((this.latencyImportance == null) ? 
				0 : this.latencyImportance.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.connectionTimeImportance);
		builder.append(this.latencyImportance);
		builder.append(this.bandwidthImportance);
		return builder.toString();
	}
}
