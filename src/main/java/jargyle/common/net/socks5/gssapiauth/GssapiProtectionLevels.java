package jargyle.common.net.socks5.gssapiauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GssapiProtectionLevels {

	public static final GssapiProtectionLevels DEFAULT_INSTANCE =
			GssapiProtectionLevels.newInstance(
					GssapiProtectionLevel.REQUIRED_INTEG_AND_CONF,
					GssapiProtectionLevel.REQUIRED_INTEG,
					GssapiProtectionLevel.NONE);
	
	public static GssapiProtectionLevels newInstance(
			final GssapiProtectionLevel gssapiProtectionLvl,
			final GssapiProtectionLevel... gssapiProtectionLvls) {
		return newInstance(gssapiProtectionLvl, Arrays.asList(
				gssapiProtectionLvls));
	}
	
	public static GssapiProtectionLevels newInstance(
			final GssapiProtectionLevel gssapiProtectionLvl,
			final List<GssapiProtectionLevel> gssapiProtectionLvls) {
		List<GssapiProtectionLevel> list = 
				new ArrayList<GssapiProtectionLevel>();
		list.add(gssapiProtectionLvl);
		list.addAll(gssapiProtectionLvls);
		return new GssapiProtectionLevels(list);
	}
	
	public static GssapiProtectionLevels newInstance(final String s) {
		List<GssapiProtectionLevel> gssapiProtectionLevels = 
				new ArrayList<GssapiProtectionLevel>();
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			gssapiProtectionLevels.add(GssapiProtectionLevel.getInstance(
					sElement));
		}
		return new GssapiProtectionLevels(gssapiProtectionLevels);
	}
	
	private final List<GssapiProtectionLevel> gssapiProtectionLevels;
	
	private GssapiProtectionLevels(
			final List<GssapiProtectionLevel> gssapiProtectionLvls) {
		this.gssapiProtectionLevels = 
				new ArrayList<GssapiProtectionLevel>(
						gssapiProtectionLvls);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GssapiProtectionLevels)) {
			return false;
		}
		GssapiProtectionLevels other = (GssapiProtectionLevels) obj;
		if (this.gssapiProtectionLevels == null) {
			if (other.gssapiProtectionLevels != null) {
				return false;
			}
		} else if (!this.gssapiProtectionLevels.equals(
				other.gssapiProtectionLevels)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.gssapiProtectionLevels == null) ? 
				0 : this.gssapiProtectionLevels.hashCode());
		return result;
	}

	public List<GssapiProtectionLevel> toList() {
		return Collections.unmodifiableList(this.gssapiProtectionLevels);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<GssapiProtectionLevel> iterator = 
				this.gssapiProtectionLevels.iterator();
				iterator.hasNext();) {
			GssapiProtectionLevel gssapiProtectionLevel = iterator.next();
			builder.append(gssapiProtectionLevel);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
	
}
