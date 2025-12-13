package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5.gssapiauthmethod.protectionLevels")
class Socks5GssapiAuthMethodProtectionLevelsXml extends ValueXml {

	@XmlElement(name = "socks5.gssapiauthmethod.protectionLevel", required = true)
	protected List<String> socks5GssapiAuthMethodProtectionLevels; 
	
	public Socks5GssapiAuthMethodProtectionLevelsXml() {
		this.socks5GssapiAuthMethodProtectionLevels = new ArrayList<String>();
	}
	
	public Socks5GssapiAuthMethodProtectionLevelsXml(
			final ProtectionLevels protectionLevels) {
		this.socks5GssapiAuthMethodProtectionLevels = new ArrayList<String>();
		for (ProtectionLevel protectionLevel : protectionLevels.toList()) {
			this.socks5GssapiAuthMethodProtectionLevels.add(
					protectionLevel.toString());
		}
	}
	
	public ProtectionLevels toProtectionLevels() {
		List<ProtectionLevel> protectionLevels =
				new ArrayList<ProtectionLevel>();
		for (String socks5GssapiAuthMethodProtectionLevel :
			this.socks5GssapiAuthMethodProtectionLevels) {
			protectionLevels.add(ProtectionLevel.valueOfString(
					socks5GssapiAuthMethodProtectionLevel));
		}
		return ProtectionLevels.of(
				protectionLevels.get(0), 
				protectionLevels.subList(1, protectionLevels.size()));
	}
	
	@Override
	public Object toValue() {
		return this.toProtectionLevels();
	}

}
