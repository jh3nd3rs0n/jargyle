package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5.gssapimethod.protectionLevels")
class Socks5GssapiMethodProtectionLevelsXml extends ValueXml {

	@XmlElement(name = "socks5.gssapimethod.protectionLevel", required = true)
	protected List<String> socks5GssapiMethodProtectionLevels; 
	
	public Socks5GssapiMethodProtectionLevelsXml() {
		this.socks5GssapiMethodProtectionLevels = new ArrayList<String>();
	}
	
	public Socks5GssapiMethodProtectionLevelsXml(
			final ProtectionLevels protectionLevels) {
		this.socks5GssapiMethodProtectionLevels = new ArrayList<String>();
		for (ProtectionLevel protectionLevel : protectionLevels.toList()) {
			this.socks5GssapiMethodProtectionLevels.add(
					protectionLevel.toString());
		}
	}
	
	public ProtectionLevels toProtectionLevels() {
		List<ProtectionLevel> protectionLevels =
				new ArrayList<ProtectionLevel>();
		for (String socks5GssapiMethodProtectionLevel :
			this.socks5GssapiMethodProtectionLevels) {
			protectionLevels.add(ProtectionLevel.valueOfString(
					socks5GssapiMethodProtectionLevel));
		}
		return ProtectionLevels.newInstance(
				protectionLevels.get(0), 
				protectionLevels.subList(1, protectionLevels.size()));
	}
	
	@Override
	public Object toValue() {
		return this.toProtectionLevels();
	}

}
