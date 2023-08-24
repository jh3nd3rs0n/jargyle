package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosAddressConstants;

public final class Host {
	
	private static final Host ALL_ZEROS_INET4_INSTANCE = Host.newInstance(
			AllZerosAddressConstants.getInet4Address());
	
	private static final Host ALL_ZEROS_INET6_INSTANCE = Host.newInstance(
			AllZerosAddressConstants.getInet6Address());
	
	public static final Host getAllZerosInet4Instance() {
		return ALL_ZEROS_INET4_INSTANCE;
	}
	
	public static final Host getAllZerosInet6Instance() {
		return ALL_ZEROS_INET6_INSTANCE;
	}
	
	private static Host newInstance(final InetAddress inetAddress) {
		return new Host(inetAddress, inetAddress.getHostAddress());
	}
	
	public static Host newInstance(final String s) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(s);
		return new Host(inetAddress, s);
	}

	private final InetAddress inetAddress;

	private final String string;
	
	private Host(final InetAddress inetAddr, final String str) {
		this.inetAddress = inetAddr;
		this.string = str;
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
		Host other = (Host) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}
	
	public InetAddress toInetAddress() {
		return this.inetAddress;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
