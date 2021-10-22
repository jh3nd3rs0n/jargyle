package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestRules", propOrder = { "socks5RequestRulesXml" }) 
class Socks5RequestRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5RequestRule")
	protected List<Socks5RequestRuleXml> socks5RequestRulesXml;
	
	public Socks5RequestRulesXml() {
		this.socks5RequestRulesXml = new ArrayList<Socks5RequestRuleXml>(); 
	}
	
	public Socks5RequestRulesXml(final Socks5RequestRules socks5RequestRules) {
		this.socks5RequestRulesXml = new ArrayList<Socks5RequestRuleXml>();
		for (Socks5RequestRule socks5RequestRule 
				: socks5RequestRules.toList()) {
			this.socks5RequestRulesXml.add(new Socks5RequestRuleXml(
					socks5RequestRule));
		}
	}
	
	public Socks5RequestRules toSocks5RequestRules() {
		List<Socks5RequestRule> socks5RequestRules = 
				new ArrayList<Socks5RequestRule>();
		for (Socks5RequestRuleXml socks5RequestRuleXml
				: this.socks5RequestRulesXml) {
			socks5RequestRules.add(socks5RequestRuleXml.toSocks5RequestRule());
		}
		return Socks5RequestRules.newInstance(socks5RequestRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5RequestRules();
	}

}
