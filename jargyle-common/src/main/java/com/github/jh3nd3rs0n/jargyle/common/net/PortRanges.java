package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PortRanges {

	private static final PortRanges DEFAULT_INSTANCE = PortRanges.newInstance(
			PortRange.getDefault());
	
	public static PortRanges getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static PortRanges newInstance(final List<PortRange> prtRanges) {
		return new PortRanges(prtRanges);
	}
	
	public static PortRanges newInstance(final PortRange... prtRanges) {
		return newInstance(Arrays.asList(prtRanges));
	}
	
	public static PortRanges newInstance(final String s) {
		List<PortRange> prtRanges = new ArrayList<PortRange>();
		if (s.isEmpty()) {
			return newInstance(prtRanges);
		}
		String[] sElements = s.split(",");
		for (String sElement : sElements) {
			PortRange prtRange = PortRange.newInstance(sElement);
			prtRanges.add(prtRange);
		}
		return newInstance(prtRanges);
	}
	
	private final List<PortRange> portRanges;
	
	private PortRanges(final List<PortRange> prtRanges) {
		this.portRanges = new ArrayList<PortRange>(prtRanges);
	}
	
	public boolean contains(final Port port) {
		for (PortRange portRange : this.portRanges) {
			if (portRange.contains(port)) {
				return true;
			}
		}
		return false;
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
		PortRanges other = (PortRanges) obj;
		if (this.portRanges == null) {
			if (other.portRanges != null) {
				return false;
			}
		} else if (!this.portRanges.equals(other.portRanges)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.portRanges == null) ? 0 : this.portRanges.hashCode());
		return result;
	}
	
	public List<PortRange> toList() {
		return Collections.unmodifiableList(this.portRanges);
	}

	@Override
	public String toString() {
		return this.portRanges.stream()
				.map(PortRange::toString)
				.collect(Collectors.joining(","));
	}
	
}
