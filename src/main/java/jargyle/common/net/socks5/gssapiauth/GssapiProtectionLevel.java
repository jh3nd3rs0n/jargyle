package jargyle.common.net.socks5.gssapiauth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.MessageProp;

import jargyle.common.cli.HelpTextParams;

public enum GssapiProtectionLevel implements HelpTextParams {

	NONE(ProtectionLevel.NONE) {
		
		private static final String DOC = "No protection";
		
		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public MessageProp newMessageProp() {
			return null;
		}
		
	},
	
	REQUIRED_INTEG(ProtectionLevel.REQUIRED_INTEG) {
		
		private static final String DOC = "Required per-message integrity";
		
		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public MessageProp newMessageProp() {
			return new MessageProp(0, false);
		}
		
	},
	
	REQUIRED_INTEG_AND_CONF(ProtectionLevel.REQUIRED_INTEG_AND_CONF) {
		
		private static final String DOC = 
				"Required per-message integrity and confidentiality";
		
		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public MessageProp newMessageProp() {
			return new MessageProp(0, true);
		}
		
	};
	
	public static GssapiProtectionLevel getInstance(final String s) {
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
	
	public static GssapiProtectionLevel valueOf(
			final ProtectionLevel level) {
		for (GssapiProtectionLevel value 
				: GssapiProtectionLevel.values()) {
			if (value.getProtectionLevel().equals(level)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<GssapiProtectionLevel> list = Arrays.asList(
				GssapiProtectionLevel.values());
		for (Iterator<GssapiProtectionLevel> iterator = list.iterator();
				iterator.hasNext();) {
			GssapiProtectionLevel value = iterator.next();
			ProtectionLevel protectionLevel = value.getProtectionLevel();
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
	
	private final ProtectionLevel protectionLevel;
	
	private GssapiProtectionLevel(final ProtectionLevel level) {
		this.protectionLevel = level;
	}
	
	public ProtectionLevel getProtectionLevel() {
		return this.protectionLevel;
	}
	
	@Override
	public final String getUsage() {
		return this.toString();
	}
	
	public abstract MessageProp newMessageProp();
	
}
