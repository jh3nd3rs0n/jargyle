package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5.gssapiauth.protectionLevels")
class Socks5GssapiauthProtectionLevelsXml extends ValueXml {

	@XmlElement(name = "socks5.gssapiauth.protectionLevel", required = true)
	protected List<String> socks5GssapiauthProtectionLevels; 
	
	public Socks5GssapiauthProtectionLevelsXml() {
		this.socks5GssapiauthProtectionLevels = new ArrayList<String>();
	}
	
	public Socks5GssapiauthProtectionLevelsXml(
			final ProtectionLevels protectionLevels) {
		this.socks5GssapiauthProtectionLevels = new ArrayList<String>();
		for (ProtectionLevel protectionLevel : protectionLevels.toList()) {
			this.socks5GssapiauthProtectionLevels.add(
					protectionLevel.toString());
		}
	}
	
	public ProtectionLevels toProtectionLevels() {
		List<ProtectionLevel> protectionLevels =
				new ArrayList<ProtectionLevel>();
		for (String socks5GssapiauthProtectionLevel :
			this.socks5GssapiauthProtectionLevels) {
			protectionLevels.add(ProtectionLevel.valueOfString(
					socks5GssapiauthProtectionLevel));
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
