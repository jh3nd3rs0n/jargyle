package com.github.jh3nd3rs0n.jargyle.internal.net;

public final class AddressRegexConstants {

	public static final String DOMAINNAME_REGEX = 
			"^([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])"
			+ "(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*$";
	
	public static final String DOMAINNAME_WITH_OPTIONAL_LEFTMOST_ASTERISK_REGEX = 
			"^(?<asterisk>\\*\\.)?([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9])"
			+ "(\\.([a-z0-9]|[a-z0-9][a-z0-9-_]*[a-z0-9]))*$";
	
	public static final String IPV4_ADDRESS_IN_1_PART_REGEX = "^[\\d]{1,10}$";
	
	public static final String IPV4_ADDRESS_IN_1_PART_WITH_OPTIONAL_CIDR_RANGE_REGEX = 
			"^[\\d]{1,10}(?<cidrRange>/[\\d]{1,2})?$";
	
	public static final String IPV4_ADDRESS_IN_2_PARTS_REGEX =
			"^[\\d]{1,3}\\.[\\d]{1,8}$";
	
	public static final String IPV4_ADDRESS_IN_2_PARTS_WITH_OPTIONAL_CIDR_RANGE_REGEX =
			"^[\\d]{1,3}\\.[\\d]{1,8}(?<cidrRange>/[\\d]{1,2})?$";
	
	public static final String IPV4_ADDRESS_IN_3_PARTS_REGEX = 
			"^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,5}$";

	public static final String IPV4_ADDRESS_IN_3_PARTS_WITH_OPTIONAL_CIDR_RANGE_REGEX = 
			"^[\\d]{1,3}\\.[\\d]{1,3}\\.[\\d]{1,5}(?<cidrRange>/[\\d]{1,2})?$";
	
	public static final String IPV4_ADDRESS_IN_4_PARTS_REGEX = 
			"^[\\d]{1,3}(\\.[\\d]{1,3}){3}+$";
	
	public static final String IPV4_ADDRESS_IN_4_PARTS_WITH_OPTIONAL_CIDR_RANGE_REGEX = 
			"^[\\d]{1,3}(\\.[\\d]{1,3}){3}(?<cidrRange>/[\\d]{1,2})?$";
	
	public static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX = 
			"^([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}$";
	
	public static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_WITH_OPTIONAL_CIDR_RANGE_REGEX = 
			"^([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}"
			+ "(?<cidrRange>/[\\d]{1,3})?$";
	
	public static final String IPV6_ADDRESS_IN_FULL_FORM_REGEX = 
			"^[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+$";
	
	public static final String IPV6_ADDRESS_IN_FULL_FORM_WITH_OPTIONAL_CIDR_RANGE_REGEX = 
			"^[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+(?<cidrRange>/[\\d]{1,3})?$";
	
	public static final int MAX_IPV4_ADDRESS_CIDR_RANGE = 32;
	
	public static final int MAX_IPV6_ADDRESS_CIDR_RANGE = 128;
	
	private AddressRegexConstants() { }
	
}
