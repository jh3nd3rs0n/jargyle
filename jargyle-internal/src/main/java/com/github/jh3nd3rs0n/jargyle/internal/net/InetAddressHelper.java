package com.github.jh3nd3rs0n.jargyle.internal.net;

import com.github.jh3nd3rs0n.jargyle.internal.regex.RegexHelper;

public final class InetAddressHelper {
	
	public static boolean isAllZerosIpAddress(final String string) {
		return isAllZerosIpv4Address(string) || isAllZerosIpv6Address(string);
	}

	public static boolean isAllZerosIpv4Address(final String string) {
		return AllZerosIpAddressConstants.IPV4_ADDRESS_AS_1_PART.equals(string)
				|| AllZerosIpAddressConstants.IPV4_ADDRESS_IN_2_PARTS.equals(string)
				|| AllZerosIpAddressConstants.IPV4_ADDRESS_IN_3_PARTS.equals(string)
				|| AllZerosIpAddressConstants.IPV4_ADDRESS_IN_4_PARTS.equals(string);
	}

	public static boolean isAllZerosIpv6Address(final String string) {
		return AllZerosIpAddressConstants.IPV6_ADDRESS_IN_COMPRESSED_FORM.equals(string)
				|| AllZerosIpAddressConstants.IPV6_ADDRESS_IN_FULL_FORM.equals(string);
	}
	
	public static boolean isDomainname(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				InetAddressRegexConstants.DOMAINNAME_REGEX));
	}

	public static boolean isInetAddress(final String string) {
		return isDomainname(string) || isIpv4Address(string) || isIpv6Address(string);
	}
	
	public static boolean isIpv4Address(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				InetAddressRegexConstants.IPV4_ADDRESS_AS_1_PART_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						InetAddressRegexConstants.IPV4_ADDRESS_AS_2_PARTS_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						InetAddressRegexConstants.IPV4_ADDRESS_AS_3_PARTS_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						InetAddressRegexConstants.IPV4_ADDRESS_AS_4_PARTS_REGEX));
	}

	public static boolean isIpv6Address(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				InetAddressRegexConstants.IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						InetAddressRegexConstants.IPV6_ADDRESS_IN_FULL_FORM_REGEX));
	}

	private InetAddressHelper() { }

}
