package jargyle.net.socks.transport.v5.gssapiauth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.MessageProp;

import jargyle.help.HelpText;

public enum GssapiProtectionLevel {

	@HelpText(doc = "No protection", usage = "NONE")
	NONE(ProtectionLevel.NONE) {

		@Override
		public MessageProp newMessageProp() {
			return null;
		}
		
	},
	
	@HelpText(doc = "Required per-message integrity", usage = "REQUIRED_INTEG")
	REQUIRED_INTEG(ProtectionLevel.REQUIRED_INTEG) {

		@Override
		public MessageProp newMessageProp() {
			return new MessageProp(0, false);
		}
		
	},
	
	@HelpText(
			doc = "Required per-message integrity and confidentiality", 
			usage = "REQUIRED_INTEG_AND_CONF"
	)
	REQUIRED_INTEG_AND_CONF(ProtectionLevel.REQUIRED_INTEG_AND_CONF) {

		@Override
		public MessageProp newMessageProp() {
			return new MessageProp(0, true);
		}
		
	};
	
	public static GssapiProtectionLevel valueOfProtectionLevel(
			final ProtectionLevel level) {
		for (GssapiProtectionLevel value 
				: GssapiProtectionLevel.values()) {
			if (value.protectionLevelValue().equals(level)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<GssapiProtectionLevel> list = Arrays.asList(
				GssapiProtectionLevel.values());
		for (Iterator<GssapiProtectionLevel> iterator = list.iterator();
				iterator.hasNext();) {
			GssapiProtectionLevel value = iterator.next();
			ProtectionLevel protectionLevel = value.protectionLevelValue();
			sb.append(protectionLevel);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected GSS-API protection level must be one of "
						+ "the following values: %s. actual value is %s",
						sb.toString(),
						level));
	}
	
	public static GssapiProtectionLevel valueOfString(final String s) {
		GssapiProtectionLevel gssapiProtectionLevel = null;
		try {
			gssapiProtectionLevel = GssapiProtectionLevel.valueOf(s); 
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<GssapiProtectionLevel> list = Arrays.asList(
					GssapiProtectionLevel.values());
			for (Iterator<GssapiProtectionLevel> iterator = list.iterator();
					iterator.hasNext();) {
				GssapiProtectionLevel value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected GSS-API protection level must be one of "
							+ "the following values: %s. actual value is %s",
							sb.toString(),
							s), 
					e);
		}
		return gssapiProtectionLevel;
	}
	
	private final ProtectionLevel protectionLevelValue;
	
	private GssapiProtectionLevel(final ProtectionLevel levelValue) {
		this.protectionLevelValue = levelValue;
	}
	
	public abstract MessageProp newMessageProp();
	
	public ProtectionLevel protectionLevelValue() {
		return this.protectionLevelValue;
	}
	
}
