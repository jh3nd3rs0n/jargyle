package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class AllZerosIpAddressConstants {

	public static final String IPV4_ADDRESS_AS_1_PART = "0";
	
	public static final String IPV4_ADDRESS_IN_2_PARTS = "0.0";
	
	public static final String IPV4_ADDRESS_IN_3_PARTS = "0.0.0";
	
	public static final String IPV4_ADDRESS_IN_4_PARTS = "0.0.0.0";
	
	public static final String IPV4_ADDRESS = IPV4_ADDRESS_IN_4_PARTS;

	public static final String IPV6_ADDRESS_IN_COMPRESSED_FORM = "::";
	
	public static final String IPV6_ADDRESS_IN_FULL_FORM = "0:0:0:0:0:0:0:0";
	
	public static final String IPV6_ADDRESS = IPV6_ADDRESS_IN_FULL_FORM;
	
	private static InetAddress inet4Address;
	
	private static InetAddress inet6Address;
	
	public static InetAddress getInet4Address() {
		if (inet4Address == null) {
			try {
				inet4Address = InetAddress.getByName(IPV4_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return inet4Address;
	}
	
	public static InetAddress getInet6Address() {
		if (inet6Address == null) {
			try {
				inet6Address = InetAddress.getByName(IPV6_ADDRESS);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return inet6Address;
	}
	
	private AllZerosIpAddressConstants() { }
	
}
