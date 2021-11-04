package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5UdpRules", propOrder = { "socks5UdpRulesXml" }) 
class Socks5UdpRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5UdpRule")
	protected List<Socks5UdpRuleXml> socks5UdpRulesXml;
	
	public Socks5UdpRulesXml() {
		this.socks5UdpRulesXml = new ArrayList<Socks5UdpRuleXml>(); 
	}
	
	public Socks5UdpRulesXml(final Socks5UdpRules socks5UdpRules) {
		this.socks5UdpRulesXml = new ArrayList<Socks5UdpRuleXml>();
		for (Socks5UdpRule socks5UdpRule : socks5UdpRules.toList()) {
			this.socks5UdpRulesXml.add(new Socks5UdpRuleXml(socks5UdpRule));
		}
	}
	
	public Socks5UdpRules toSocks5UdpRules() {
		List<Socks5UdpRule> socks5UdpRules = new ArrayList<Socks5UdpRule>();
		for (Socks5UdpRuleXml socks5UdpRuleXml : this.socks5UdpRulesXml) {
			socks5UdpRules.add(socks5UdpRuleXml.toSocks5UdpRule());
		}
		return Socks5UdpRules.newInstance(socks5UdpRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5UdpRules();
	}

}
