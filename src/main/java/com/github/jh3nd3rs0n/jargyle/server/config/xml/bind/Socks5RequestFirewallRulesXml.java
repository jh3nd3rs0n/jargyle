package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestFirewallRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(
		name = "socks5RequestFirewallRules", 
		propOrder = { "socks5RequestFirewallRulesXml" }
) 
class Socks5RequestFirewallRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5RequestFirewallRule")
	protected List<Socks5RequestFirewallRuleXml> socks5RequestFirewallRulesXml;
	
	public Socks5RequestFirewallRulesXml() {
		this.socks5RequestFirewallRulesXml = 
				new ArrayList<Socks5RequestFirewallRuleXml>(); 
	}
	
	public Socks5RequestFirewallRulesXml(
			final Socks5RequestFirewallRules socks5RequestFirewallRules) {
		this.socks5RequestFirewallRulesXml = 
				new ArrayList<Socks5RequestFirewallRuleXml>();
		for (Socks5RequestFirewallRule socks5RequestFirewallRule 
				: socks5RequestFirewallRules.toList()) {
			this.socks5RequestFirewallRulesXml.add(
					new Socks5RequestFirewallRuleXml(
							socks5RequestFirewallRule));
		}
	}
	
	public Socks5RequestFirewallRules toSocks5RequestFirewallRules() {
		List<Socks5RequestFirewallRule> socks5RequestFirewallRules = 
				new ArrayList<Socks5RequestFirewallRule>();
		for (Socks5RequestFirewallRuleXml socks5RequestFirewallRuleXml
				: this.socks5RequestFirewallRulesXml) {
			socks5RequestFirewallRules.add(
					socks5RequestFirewallRuleXml.toSocks5RequestFirewallRule());
		}
		return Socks5RequestFirewallRules.newInstance(
				socks5RequestFirewallRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5RequestFirewallRules();
	}

}
