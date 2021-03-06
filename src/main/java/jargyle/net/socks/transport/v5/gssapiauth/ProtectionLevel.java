package jargyle.net.socks.transport.v5.gssapiauth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.ietf.jgss.MessageProp;

import jargyle.internal.help.HelpText;
import jargyle.util.UnsignedByte;

public enum ProtectionLevel {
	
	@HelpText(doc = "No protection", usage = "NONE")
	NONE((byte) 0x00) {
		
		@Override
		public Optional<MessageProp> getMessageProp() {
			return Optional.empty();
		}
		
	},
	
	@HelpText(doc = "Required per-message integrity", usage = "REQUIRED_INTEG")
	REQUIRED_INTEG((byte) 0x01) {
		
		@Override
		public Optional<MessageProp> getMessageProp() {
			return Optional.of(new MessageProp(0, false));
		}
		
	},
	
	@HelpText(
			doc = "Required per-message integrity and confidentiality", 
			usage = "REQUIRED_INTEG_AND_CONF"
	)	
	REQUIRED_INTEG_AND_CONF((byte) 0x02) {
		
		@Override
		public Optional<MessageProp> getMessageProp() {
			return Optional.of(new MessageProp(0, true));
		}
		
	},
	
	SELECTIVE_INTEG_OR_CONF((byte) 0x03) {
		
		@Override
		public Optional<MessageProp> getMessageProp() {
			return Optional.of(new MessageProp(0, true));
		}
		
	};
	
	public static ProtectionLevel valueOfByte(final byte b) {
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
	
	public static ProtectionLevel valueOfString(final String s) {
		ProtectionLevel protectionLevel = null;
		try {
			protectionLevel = ProtectionLevel.valueOf(s); 
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<ProtectionLevel> list = Arrays.asList(
					ProtectionLevel.values());
			for (Iterator<ProtectionLevel> iterator = list.iterator();
					iterator.hasNext();) {
				ProtectionLevel value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected protection level must be one of the "
							+ "following values: %s. actual value is %s",
							sb.toString(),
							s), 
					e);
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
	
	public abstract Optional<MessageProp> getMessageProp();
	
}