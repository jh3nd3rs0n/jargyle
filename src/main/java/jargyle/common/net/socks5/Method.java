package jargyle.common.net.socks5;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.common.util.UnsignedByte;

public enum Method {

	NO_AUTHENTICATION_REQUIRED((byte) 0x00),
	
	GSSAPI((byte) 0x01),
	
	USERNAME_PASSWORD((byte) 0x02),
	
	NO_ACCEPTABLE_METHODS((byte) 0xff);

	public static Method valueOf(final byte b) {
		for (Method method : Method.values()) {
			if (method.byteValue() == b) {
				return method;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Method> list = Arrays.asList(Method.values());
		for (Iterator<Method> iterator = list.iterator(); 
				iterator.hasNext();) {
			Method value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected method must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	private final byte byteValue;
	
	private Method(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}

}
