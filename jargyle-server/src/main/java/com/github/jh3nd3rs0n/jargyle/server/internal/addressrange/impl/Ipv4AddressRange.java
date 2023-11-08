package com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressRegexConstants;
import com.github.jh3nd3rs0n.jargyle.internal.regex.RegexHelper;
import com.github.jh3nd3rs0n.jargyle.server.AddressRange;

public final class Ipv4AddressRange extends AddressRange {
	
	private static final String ADDRESS_AS_1_PART_REGEX =
			InetAddressRegexConstants.IPV4_ADDRESS_AS_1_PART_REGEX;
	
	private static final String ADDRESS_AS_2_PARTS_REGEX =
			InetAddressRegexConstants.IPV4_ADDRESS_AS_2_PARTS_REGEX;
	
	private static final String ADDRESS_AS_3_PARTS_REGEX = 
			InetAddressRegexConstants.IPV4_ADDRESS_AS_3_PARTS_REGEX;
	
	private static final String ADDRESS_AS_4_PARTS_REGEX =
			InetAddressRegexConstants.IPV4_ADDRESS_AS_4_PARTS_REGEX;
	
	private static final String FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_1_PART_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_1_PART_REGEX)
			.append('-')
			.append(ADDRESS_AS_1_PART_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_2_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_1_PART_REGEX)
			.append('-')
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_3_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_1_PART_REGEX)
			.append('-')
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_4_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_1_PART_REGEX)
			.append('-')
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_1_PART_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_1_PART_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.toString(); 
	
	private static final String FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_1_PART_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_1_PART_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_1_PART_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_1_PART_REGEX)
			.toString(); 
	
	private static final String FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_2_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_3_PARTS_REGEX)
			.toString();
	
	private static final String FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX = 
			new StringBuilder()
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.append('-')
			.append(ADDRESS_AS_4_PARTS_REGEX)
			.toString();

	public static boolean isIpv4AddressRange(final String s) {
		return InetAddressHelper.isIpv4Address(s)
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_1_PART_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_2_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_3_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_1_PART_TO_ADDRESS_AS_4_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_1_PART_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_2_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_1_PART_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_3_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_1_PART_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_2_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_3_PARTS_REGEX))
				|| s.matches(RegexHelper.getRegexWithInputBoundaries(
						FROM_ADDRESS_AS_4_PARTS_TO_ADDRESS_AS_4_PARTS_REGEX)); 
	}
	
	public static Ipv4AddressRange newInstance(final String s) {
		String message = "IPv4 address range must be in the following formats: "
				+ "IPV4_ADDRESS, "
				+ "IPV4_ADDRESS1-IPV4_ADDRESS2";
		String[] sElements = s.split("-");
		if (sElements.length < 1 || sElements.length > 2 
				|| (sElements.length == 1 && s.indexOf('-') != -1)) {
			throw new IllegalArgumentException(message);
		}
		if (sElements.length == 1) {
			String address = sElements[0];
			Ipv4AddressRange ipv4AddressRange = null;
			try {
				ipv4AddressRange = new Ipv4AddressRange(address, address);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(message, e);
			}
			return ipv4AddressRange;
		}
		String startingAddress = sElements[0];
		String endingAddress = sElements[1];
		return new Ipv4AddressRange(startingAddress, endingAddress);
	}
	
	private final String endingAddress;
	private final InetAddress endingInetAddress;
	private final String startingAddress;
	private final InetAddress startingInetAddress;
	
	private Ipv4AddressRange(
			final String startingAddr, final String endingAddr) {
		InetAddress startingInetAddr = null;
		try {
			startingInetAddr = InetAddress.getByName(startingAddr);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		InetAddress endingInetAddr = null;
		try {
			endingInetAddr = InetAddress.getByName(endingAddr);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		if (InetAddressComparator.INSTANCE.compare(
				startingInetAddr, endingInetAddr) > 0) {
			throw new IllegalArgumentException(String.format(
					"staring address (%s) must not be greater than ending address (%s)", 
					startingAddr,
					endingAddr));
		}
		this.endingAddress = endingAddr;
		this.endingInetAddress = endingInetAddr;
		this.startingAddress = startingAddr;
		this.startingInetAddress = startingInetAddr;
	}
	
	@Override
	public boolean contains(final String address) {
		if (!InetAddressHelper.isIpv4Address(address)) {
			return false;
		}
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return InetAddressComparator.INSTANCE.compare(
				this.startingInetAddress, inetAddress) <= 0	
				&& InetAddressComparator.INSTANCE.compare(
						this.endingInetAddress, inetAddress) >= 0;
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
		Ipv4AddressRange other = (Ipv4AddressRange) obj;
		if (this.endingInetAddress == null) {
			if (other.endingInetAddress != null) {
				return false;
			}
		} else if (!this.endingInetAddress.equals(other.endingInetAddress)) {
			return false;
		}
		if (this.startingInetAddress == null) {
			if (other.startingInetAddress != null) {
				return false;
			}
		} else if (!this.startingInetAddress.equals(other.startingInetAddress)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.endingInetAddress == null) ? 
				0 : this.endingInetAddress.hashCode());
		result = prime * result + ((this.startingInetAddress == null) ? 
				0 : this.startingInetAddress.hashCode());
		return result;
	}

	@Override
	public String toString() {
		if (this.startingAddress.equals(this.endingAddress)) {
			return this.startingAddress;
		}
		return String.format("%s-%s", this.startingAddress, this.endingAddress);
	}

}
