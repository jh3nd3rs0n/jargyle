package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public enum ProtectionLevel {
	
	@HelpText(doc = "No protection", usage = "NONE")
	NONE((byte) 0x00) {
		
		@Override
		public MessageProp getMessageProp() {
			return null;
		}
		
	},
	
	@HelpText(doc = "Required per-message integrity", usage = "REQUIRED_INTEG")
	REQUIRED_INTEG((byte) 0x01) {
		
		@Override
		public MessageProp getMessageProp() {
			return new MessageProp(0, false);
		}
		
	},
	
	@HelpText(
			doc = "Required per-message integrity and confidentiality", 
			usage = "REQUIRED_INTEG_AND_CONF"
	)	
	REQUIRED_INTEG_AND_CONF((byte) 0x02) {
		
		@Override
		public MessageProp getMessageProp() {
			return new MessageProp(0, true);
		}
		
	},
	
	SELECTIVE_INTEG_OR_CONF((byte) 0x03) {
		
		@Override
		public MessageProp getMessageProp() {
			return new MessageProp(0, true);
		}
		
	};
	
	public static ProtectionLevel valueOfByte(final byte b) {
		for (ProtectionLevel protectionLevel : ProtectionLevel.values()) {
			if (protectionLevel.byteValue() == b) {
				return protectionLevel;
			}
		}
		String str = Arrays.stream(ProtectionLevel.values())
				.map(ProtectionLevel::byteValue)
				.map(bv -> UnsignedByte.newInstance(bv).intValue())
				.map(i -> Integer.toHexString(i))
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected protection level must be one of the following "
				+ "values: %s. actual value is %s",
				str,
				Integer.toHexString(UnsignedByte.newInstance(b).intValue())));
	}
	
	public static ProtectionLevel valueOfString(final String s) {
		ProtectionLevel protectionLevel = null;
		try {
			protectionLevel = ProtectionLevel.valueOf(s); 
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(ProtectionLevel.values())
					.map(ProtectionLevel::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected protection level must be one of the following "
					+ "values: %s. actual value is %s",
					str,
					s));
		}
		return protectionLevel;		
	}
	
	private final byte byteValue;
	
	private ProtectionLevel(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
	public abstract MessageProp getMessageProp();
	
}