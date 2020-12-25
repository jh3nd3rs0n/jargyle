package jargyle.common.net.ssl;

import java.util.Arrays;
import java.util.Iterator;

public final class Protocols {

	public static Protocols newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split("\\s"));
	}
	
	public static Protocols newInstance(final String[] prtcls) {
		return new Protocols(prtcls);
	}
	
	private final String[] protocols;
	
	private Protocols(final String[] prtcls) {
		this.protocols = Arrays.copyOf(prtcls, prtcls.length);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = Arrays.asList(this.protocols).iterator(); 
				iterator.hasNext();) {
			String protocol = iterator.next();
			builder.append(protocol.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();		
	}
	
	public String[] toStringArray() {
		return Arrays.copyOf(this.protocols, this.protocols.length);
	}

}
