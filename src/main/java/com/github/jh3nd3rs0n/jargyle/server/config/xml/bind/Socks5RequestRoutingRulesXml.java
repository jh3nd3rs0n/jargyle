package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5RequestRoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5RequestRoutingRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(
		name = "socks5RequestRoutingRules",	
		propOrder = { "socks5RequestRoutingRulesXml" }
) 
class Socks5RequestRoutingRulesXml extends ValueXml {

	@XmlElement(name = "socks5RequestRoutingRule")
	protected List<Socks5RequestRoutingRuleXml> socks5RequestRoutingRulesXml;

	public Socks5RequestRoutingRulesXml() {
		this.socks5RequestRoutingRulesXml = 
				new ArrayList<Socks5RequestRoutingRuleXml>();
	}
	
	public Socks5RequestRoutingRulesXml(
			final Socks5RequestRoutingRules socks5RequestRoutingRules) {
		this.socks5RequestRoutingRulesXml = 
				new ArrayList<Socks5RequestRoutingRuleXml>();
		for (Socks5RequestRoutingRule socks5RequestRoutingRule
				: socks5RequestRoutingRules.toList()) {
			this.socks5RequestRoutingRulesXml.add(
					new Socks5RequestRoutingRuleXml(socks5RequestRoutingRule));
		}
	}
	
	public Socks5RequestRoutingRules toSocks5RequestRoutingRules() {
		List<Socks5RequestRoutingRule> socks5RequestRoutingRules = 
				new ArrayList<Socks5RequestRoutingRule>();
		for (Socks5RequestRoutingRuleXml socks5RequestRoutingRuleXml 
				: this.socks5RequestRoutingRulesXml) {
			socks5RequestRoutingRules.add(
					socks5RequestRoutingRuleXml.toSocks5RequestRoutingRule());
		}
		return Socks5RequestRoutingRules.newInstance(socks5RequestRoutingRules); 
	}
	
	@Override
	public Object toValue() {
		return this.toSocks5RequestRoutingRules();
	}
	
}
