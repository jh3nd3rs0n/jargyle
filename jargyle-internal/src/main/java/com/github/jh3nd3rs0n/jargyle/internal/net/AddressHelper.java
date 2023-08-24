package com.github.jh3nd3rs0n.jargyle.internal.net;

import com.github.jh3nd3rs0n.jargyle.internal.regex.RegexHelper;

public final class AddressHelper {
	
	public static boolean isAllZerosAddress(final String address) {
		return isAllZerosIpv4Address(address) || isAllZerosIpv6Address(address);
	}

	public static boolean isAllZerosIpv4Address(final String address) {
		return AllZerosAddressConstants.IPV4_ADDRESS_AS_1_PART.equals(address)
				|| AllZerosAddressConstants.IPV4_ADDRESS_IN_2_PARTS.equals(address)
				|| AllZerosAddressConstants.IPV4_ADDRESS_IN_3_PARTS.equals(address)
				|| AllZerosAddressConstants.IPV4_ADDRESS_IN_4_PARTS.equals(address);
	}

	public static boolean isAllZerosIpv6Address(final String address) {
		return AllZerosAddressConstants.IPV6_ADDRESS_IN_COMPRESSED_FORM.equals(address)
				|| AllZerosAddressConstants.IPV6_ADDRESS_IN_FULL_FORM.equals(address);
	}
	
	public static boolean isDomainname(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				AddressRegexConstants.DOMAINNAME_REGEX));
	}

	public static boolean isIpv4Address(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				AddressRegexConstants.IPV4_ADDRESS_AS_1_PART_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						AddressRegexConstants.IPV4_ADDRESS_AS_2_PARTS_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						AddressRegexConstants.IPV4_ADDRESS_AS_3_PARTS_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						AddressRegexConstants.IPV4_ADDRESS_AS_4_PARTS_REGEX));
	}

	public static boolean isIpv6Address(final String string) {
		return string.matches(RegexHelper.getRegexWithInputBoundaries(
				AddressRegexConstants.IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX))
				|| string.matches(RegexHelper.getRegexWithInputBoundaries(
						AddressRegexConstants.IPV6_ADDRESS_IN_FULL_FORM_REGEX));
	}

	private AddressHelper() { }

}
