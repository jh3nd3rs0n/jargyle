package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class InetAddressHelper {

	public static final String INET4_ALL_ZEROS_HOST_ADDRESS = "0.0.0.0";
	
	public static final String INET6_ALL_ZEROS_HOST_ADDRESS = "0:0:0:0:0:0:0:0";
	
	private static InetAddress inet4AllZerosAddress;
	
	private static InetAddress inet6AllZerosAddress;
	
	public static InetAddress getInet4AllZerosAddress() {
		if (inet4AllZerosAddress == null) {
			try {
				inet4AllZerosAddress = InetAddress.getByName(
						INET4_ALL_ZEROS_HOST_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return inet4AllZerosAddress;
	}
	
	public static InetAddress getInet6AllZerosAddress() {
		if (inet6AllZerosAddress == null) {
			try {
				inet6AllZerosAddress = InetAddress.getByName(
						INET6_ALL_ZEROS_HOST_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return inet6AllZerosAddress;
	}
	
	public static boolean isAllZerosHostAddress(final String hostAddress) {
		return isInet4AllZerosHostAddress(hostAddress)
				|| isInet6AllZerosHostAddress(hostAddress);
	}
	
	public static boolean isInet4AllZerosHostAddress(
			final String hostAddress) {
		return INET4_ALL_ZEROS_HOST_ADDRESS.equals(hostAddress);
	}
	
	public static boolean isInet6AllZerosHostAddress(
			final String hostAddress) {
		return INET6_ALL_ZEROS_HOST_ADDRESS.equals(hostAddress);
	}
	
	private InetAddressHelper() { }
	
}
