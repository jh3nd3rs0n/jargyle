package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

@ValuesValueTypeDoc(
		description = "",
		elementValueType = ProtectionLevel.class,
		name = "SOCKS5 GSS-API Method Protection Levels",
		syntax = "SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL2[...]]",
		syntaxName = "SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS"
)
public final class ProtectionLevels {

	private static final ProtectionLevels DEFAULT_INSTANCE =
			ProtectionLevels.newInstance(
					ProtectionLevel.REQUIRED_INTEG_AND_CONF,
					ProtectionLevel.REQUIRED_INTEG,
					ProtectionLevel.NONE);
	
	public static ProtectionLevels getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static ProtectionLevels newInstance(
			final ProtectionLevel protectionLvl,
			final List<ProtectionLevel> protectionLvls) {
		List<ProtectionLevel> list = new ArrayList<ProtectionLevel>();
		list.add(protectionLvl);
		list.addAll(protectionLvls);
		return new ProtectionLevels(list);
	}
	
	public static ProtectionLevels newInstance(
			final ProtectionLevel protectionLvl,
			final ProtectionLevel... protectionLvls) {
		return newInstance(protectionLvl, Arrays.asList(protectionLvls));
	}
	
	public static ProtectionLevels newInstance(final String s) {
		List<ProtectionLevel> protectionLevels = 
				new ArrayList<ProtectionLevel>();
		String[] sElements = s.split(",");
		for (String sElement : sElements) {
			protectionLevels.add(ProtectionLevel.valueOfString(sElement));
		}
		return new ProtectionLevels(protectionLevels);
	}
	
	private final List<ProtectionLevel> protectionLevels;
	
	private ProtectionLevels(final List<ProtectionLevel> protectionLvls) {
		this.protectionLevels =	new ArrayList<ProtectionLevel>(protectionLvls);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ProtectionLevels other = (ProtectionLevels) obj;
		if (this.protectionLevels == null) {
			if (other.protectionLevels != null) {
				return false;
			}
		} else if (!this.protectionLevels.equals(
				other.protectionLevels)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.protectionLevels == null) ? 
				0 : this.protectionLevels.hashCode());
		return result;
	}

	public List<ProtectionLevel> toList() {
		return Collections.unmodifiableList(this.protectionLevels);
	}
	
	@Override
	public String toString() {
		return this.protectionLevels.stream()
				.map(ProtectionLevel::toString)
				.collect(Collectors.joining(","));
	}
	
}
