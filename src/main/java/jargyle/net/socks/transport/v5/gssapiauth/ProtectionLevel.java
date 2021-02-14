package jargyle.net.socks.transport.v5.gssapiauth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.util.UnsignedByte;

public enum ProtectionLevel {
	
	NONE((byte) 0x00),
	
	REQUIRED_INTEG((byte) 0x01),
	
	REQUIRED_INTEG_AND_CONF((byte) 0x02),
	
	SELECTIVE_INTEG_OR_CONF((byte) 0x03);
	
	public static ProtectionLevel valueOf(final byte b) {
		for (ProtectionLevel protectionLevel : ProtectionLevel.values()) {
			if (protectionLevel.byteValue() == b) {
				return protectionLevel;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<ProtectionLevel> list = Arrays.asList(ProtectionLevel.values());
		for (Iterator<ProtectionLevel> iterator = list.iterator();
				iterator.hasNext();) {
			ProtectionLevel value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected protection level must be one of "
						+ "the following values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	private final byte byteValue;
	
	private ProtectionLevel(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}