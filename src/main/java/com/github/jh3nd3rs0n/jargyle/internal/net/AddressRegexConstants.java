package com.github.jh3nd3rs0n.jargyle.internal.net;

import com.github.jh3nd3rs0n.jargyle.internal.regex.RegexHelper;

public final class AddressRegexConstants {

	public static final String DOMAINNAME_REGEX = 
			"([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])"
			+ "(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*";
	
	public static final String IPV4_ADDRESS_AS_1_PART_REGEX = "[\\d]{1,10}";
	
	public static final String IPV4_ADDRESS_AS_2_PARTS_REGEX =
			"[\\d]{1,3}\\.[\\d]{1,8}";
	
	public static final String IPV4_ADDRESS_AS_3_PARTS_REGEX = 
			"[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,5}";
	
	public static final String IPV4_ADDRESS_AS_4_PARTS_REGEX = 
			"[\\d]{1,3}(\\.[\\d]{1,3}){3}+";
	
	public static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX = 
			"([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}";
	
	public static final String IPV6_ADDRESS_IN_FULL_FORM_REGEX = 
			"[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+";

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
	
	private AddressRegexConstants() { }
	
}
