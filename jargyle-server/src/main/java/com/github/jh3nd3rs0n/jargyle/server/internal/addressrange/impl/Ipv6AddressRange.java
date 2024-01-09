package com.github.jh3nd3rs0n.jargyle.server.internal.addressrange.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv6Address;
import com.github.jh3nd3rs0n.jargyle.server.AddressRange;

public final class Ipv6AddressRange extends AddressRange {

	public static Ipv6AddressRange newInstanceOf(final String s) {
		String message = "IPv6 address range must be in the following formats: "
				+ "IPV6_ADDRESS, "
				+ "IPV6_ADDRESS1-IPV6_ADDRESS2";
		String[] sElements = s.split("-");
		if (sElements.length < 1 || sElements.length > 2 
				|| (sElements.length == 1 && s.indexOf('-') != -1)) {
			throw new IllegalArgumentException(message);
		}
		if (sElements.length == 1) {
			String address = sElements[0];
			if (!(Host.newInstanceOf(address) instanceof HostIpv6Address)) {
				throw new IllegalArgumentException(message);
			}
			Ipv6AddressRange ipv4AddressRange = null;
			try {
				ipv4AddressRange = new Ipv6AddressRange(address, address);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(message, e);
			}
			return ipv4AddressRange;
		}
		String startingAddress = sElements[0];
		String endingAddress = sElements[1];
		if (!(Host.newInstanceOf(startingAddress) instanceof HostIpv6Address)
				|| !(Host.newInstanceOf(endingAddress) instanceof HostIpv6Address)) {
			throw new IllegalArgumentException(message);
		}
		return new Ipv6AddressRange(startingAddress, endingAddress);
	}
	
	private final String endingAddress;
	private final InetAddress endingInetAddress;
	private final String startingAddress;
	private final InetAddress startingInetAddress;
	
	private Ipv6AddressRange(
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
		Host host = Host.newInstanceOf(address);
		if (!(host instanceof HostIpv6Address)) {
			return false;
		}
		InetAddress inetAddress = null;
		try {
			inetAddress = host.toInetAddress();
		} catch (UnknownHostException e) {
			throw new AssertionError(e);
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
		Ipv6AddressRange other = (Ipv6AddressRange) obj;
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
