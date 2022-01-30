package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5ReplyFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl.Socks5ReplyFirewallRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(
		name = "socks5ReplyFirewallRules", 
		propOrder = { "socks5ReplyFirewallRulesXml" }
) 
class Socks5ReplyFirewallRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5ReplyFirewallRule")
	protected List<Socks5ReplyFirewallRuleXml> socks5ReplyFirewallRulesXml;
	
	public Socks5ReplyFirewallRulesXml() {
		this.socks5ReplyFirewallRulesXml = 
				new ArrayList<Socks5ReplyFirewallRuleXml>(); 
	}
	
	public Socks5ReplyFirewallRulesXml(
			final Socks5ReplyFirewallRules socks5ReplyFirewallRules) {
		this.socks5ReplyFirewallRulesXml = 
				new ArrayList<Socks5ReplyFirewallRuleXml>();
		for (Socks5ReplyFirewallRule socks5ReplyFirewallRule 
				: socks5ReplyFirewallRules.toList()) {
			this.socks5ReplyFirewallRulesXml.add(new Socks5ReplyFirewallRuleXml(
					socks5ReplyFirewallRule));
		}
	}
	
	public Socks5ReplyFirewallRules toSocks5ReplyFirewallRules() {
		List<Socks5ReplyFirewallRule> socks5ReplyFirewallRules = 
				new ArrayList<Socks5ReplyFirewallRule>();
		for (Socks5ReplyFirewallRuleXml socks5ReplyFirewallRuleXml 
				: this.socks5ReplyFirewallRulesXml) {
			socks5ReplyFirewallRules.add(
					socks5ReplyFirewallRuleXml.toSocks5ReplyFirewallRule());
		}
		return Socks5ReplyFirewallRules.newInstance(socks5ReplyFirewallRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5ReplyFirewallRules();
	}

}
