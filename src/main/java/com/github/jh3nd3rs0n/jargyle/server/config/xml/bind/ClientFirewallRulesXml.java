package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.ClientFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.ClientFirewallRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientFirewallRules", propOrder = { "clientFirewallRulesXml" }) 
class ClientFirewallRulesXml extends ValueXml {
	
	@XmlElement(name = "clientFirewallRule")
	protected List<ClientFirewallRuleXml> clientFirewallRulesXml;
	
	public ClientFirewallRulesXml() {
		this.clientFirewallRulesXml = new ArrayList<ClientFirewallRuleXml>();
	}
	
	public ClientFirewallRulesXml(
			final ClientFirewallRules clientFirewallRules) {
		this.clientFirewallRulesXml = new ArrayList<ClientFirewallRuleXml>();
		for (ClientFirewallRule clientFirewallRule 
				: clientFirewallRules.toList()) {
			this.clientFirewallRulesXml.add(new ClientFirewallRuleXml(
					clientFirewallRule));
		}
	}
	
	public ClientFirewallRules toClientFirewallRules() {
		List<ClientFirewallRule> clientFirewallRules = 
				new ArrayList<ClientFirewallRule>();
		for (ClientFirewallRuleXml clientFirewallRuleXml 
				: this.clientFirewallRulesXml) {
			clientFirewallRules.add(clientFirewallRuleXml.toClientFirewallRule());
		}
		return ClientFirewallRules.newInstance(clientFirewallRules);
	}

	@Override
	public Object toValue() {
		return this.toClientFirewallRules();
	}

}
