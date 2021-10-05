package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class AllZerosInetAddressHelper {

	public static final String ALL_ZEROS_INET4_HOST_ADDRESS = "0.0.0.0";
	
	public static final String ALL_ZEROS_INET6_HOST_ADDRESS = "0:0:0:0:0:0:0:0";
	
	private static InetAddress allZerosInet4Address;
	
	private static InetAddress allZerosInet6Address;
	
	public static InetAddress getAllZerosInet4Address() {
		if (allZerosInet4Address == null) {
			try {
				allZerosInet4Address = InetAddress.getByName(
						ALL_ZEROS_INET4_HOST_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return allZerosInet4Address;
	}
	
	public static InetAddress getAllZerosInet6Address() {
		if (allZerosInet6Address == null) {
			try {
				allZerosInet6Address = InetAddress.getByName(
						ALL_ZEROS_INET6_HOST_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return allZerosInet6Address;
	}
	
	public static boolean isAllZerosHostAddress(final String hostAddress) {
		return isAllZerosInet4HostAddress(hostAddress)
				|| isAllZerosInet6HostAddress(hostAddress);
	}
	
	public static boolean isAllZerosInet4HostAddress(
			final String hostAddress) {
		return ALL_ZEROS_INET4_HOST_ADDRESS.equals(hostAddress);
	}
	
	public static boolean isAllZerosInet6HostAddress(
			final String hostAddress) {
		return ALL_ZEROS_INET6_HOST_ADDRESS.equals(hostAddress);
	}
	
	private AllZerosInetAddressHelper() { }
	
}
