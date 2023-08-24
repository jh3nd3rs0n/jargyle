package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.ServerSocket;
import java.net.Socket;

public final class PerformancePreferences {
	
	public static final int MAX_IMPORTANCE_VALUE = 2;
	
	public static final int MIN_IMPORTANCE_VALUE = 0;
	
	private static final String REGEX = String.format(
			"^[%1$s-%2$s][%1$s-%2$s][%1$s-%2$s]$", 
			MIN_IMPORTANCE_VALUE, 
			MAX_IMPORTANCE_VALUE);
	
	public static PerformancePreferences newInstance(
			final int connectionTime, final int latency, final int bandwidth) {
		return new PerformancePreferences(connectionTime, latency, bandwidth);
	}
	
	public static PerformancePreferences newInstance(
			final PerformancePreferences other) {
		return new PerformancePreferences(other);
	}
	
	public static PerformancePreferences newInstance(final String s) {
		if (!s.matches(REGEX)) {
			throw new IllegalArgumentException(String.format(
					"must be a string consisting of 3 digits each "
					+ "between %s and %s (inclusive)", 
					MIN_IMPORTANCE_VALUE,
					MAX_IMPORTANCE_VALUE));
		}
		char[] sChars = s.toCharArray();
		int connectionTime = Integer.parseInt(Character.toString(sChars[0]));
		int latency = Integer.parseInt(Character.toString(sChars[1]));
		int bandwidth = Integer.parseInt(Character.toString(sChars[2]));
		return new PerformancePreferences(connectionTime, latency, bandwidth);
	}
	
	private final int bandwidthImportance;
	private final int connectionTimeImportance;
	private final int latencyImportance;
	
	private PerformancePreferences(
			final int connectionTime, final int latency, final int bandwidth) {
		if (connectionTime < MIN_IMPORTANCE_VALUE 
				|| connectionTime > MAX_IMPORTANCE_VALUE) {
			throw new IllegalArgumentException(String.format(
					"connection time importance must be between "
					+ "%s and %s (inclusive)", 
					MIN_IMPORTANCE_VALUE,
					MAX_IMPORTANCE_VALUE));
		}
		if (latency < MIN_IMPORTANCE_VALUE || latency > MAX_IMPORTANCE_VALUE) {
			throw new IllegalArgumentException(String.format(
					"latency importance must be between "
					+ "%s and %s (inclusive)", 
					MIN_IMPORTANCE_VALUE,
					MAX_IMPORTANCE_VALUE));
		}
		if (bandwidth < MIN_IMPORTANCE_VALUE 
				|| bandwidth > MAX_IMPORTANCE_VALUE) {
			throw new IllegalArgumentException(String.format(
					"bandwidth importance must be between "
					+ "%s and %s (inclusive)", 
					MIN_IMPORTANCE_VALUE,
					MAX_IMPORTANCE_VALUE));
		}
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
				this.connectionTimeImportance, 
				this.latencyImportance, 
				this.bandwidthImportance);
	}
	public void applyTo(final Socket Socket) {
		Socket.setPerformancePreferences(
				this.connectionTimeImportance, 
				this.latencyImportance, 
				this.bandwidthImportance);
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
		PerformancePreferences other = (PerformancePreferences) obj;
		if (this.bandwidthImportance != other.bandwidthImportance) {
			return false;
		}
		if (this.connectionTimeImportance != other.connectionTimeImportance) {
			return false;
		}
		if (this.latencyImportance != other.latencyImportance) {
			return false;
		}
		return true;
	}
	
	public int getBandwidthImportance() {
		return this.bandwidthImportance;
	}
	
	public int getConnectionTimeImportance() {
		return this.connectionTimeImportance;
	}
	
	public int getLatencyImportance() {
		return this.latencyImportance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.bandwidthImportance;
		result = prime * result + this.connectionTimeImportance;
		result = prime * result + this.latencyImportance;
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
