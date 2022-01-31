package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(
		name = "socks5UdpFirewallRules", 
		propOrder = { "socks5UdpFirewallRulesXml" }
) 
class Socks5UdpFirewallRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5UdpFirewallRule")
	protected List<Socks5UdpFirewallRuleXml> socks5UdpFirewallRulesXml;
	
	public Socks5UdpFirewallRulesXml() {
		this.socks5UdpFirewallRulesXml = 
				new ArrayList<Socks5UdpFirewallRuleXml>(); 
	}
	
	public Socks5UdpFirewallRulesXml(
			final Socks5UdpFirewallRules socks5UdpFirewallRules) {
		this.socks5UdpFirewallRulesXml = 
				new ArrayList<Socks5UdpFirewallRuleXml>();
		for (Socks5UdpFirewallRule socks5UdpFirewallRule 
				: socks5UdpFirewallRules.toList()) {
			this.socks5UdpFirewallRulesXml.add(new Socks5UdpFirewallRuleXml(
					socks5UdpFirewallRule));
		}
	}
	
	public Socks5UdpFirewallRules toSocks5UdpFirewallRules() {
		List<Socks5UdpFirewallRule> socks5UdpFirewallRules = 
				new ArrayList<Socks5UdpFirewallRule>();
		for (Socks5UdpFirewallRuleXml socks5UdpFirewallRuleXml 
				: this.socks5UdpFirewallRulesXml) {
			socks5UdpFirewallRules.add(
					socks5UdpFirewallRuleXml.toSocks5UdpFirewallRule());
		}
		return Socks5UdpFirewallRules.newInstance(socks5UdpFirewallRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5UdpFirewallRules();
	}

}
