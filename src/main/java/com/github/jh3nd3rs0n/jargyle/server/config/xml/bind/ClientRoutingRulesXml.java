package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.ClientRoutingRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientRoutingRules",	propOrder = { "clientRoutingRulesXml" }) 
class ClientRoutingRulesXml extends ValueXml {

	@XmlElement(name = "clientRoutingRule")
	protected List<ClientRoutingRuleXml> clientRoutingRulesXml;

	public ClientRoutingRulesXml() {
		this.clientRoutingRulesXml = new ArrayList<ClientRoutingRuleXml>();
	}
	
	public ClientRoutingRulesXml(final ClientRoutingRules clientRoutingRules) {
		this.clientRoutingRulesXml = new ArrayList<ClientRoutingRuleXml>();
		for (ClientRoutingRule clientRoutingRule
				: clientRoutingRules.toList()) {
			this.clientRoutingRulesXml.add(new ClientRoutingRuleXml(
					clientRoutingRule));
		}
	}
	
	public ClientRoutingRules toClientRoutingRules() {
		List<ClientRoutingRule> clientRoutingRules = 
				new ArrayList<ClientRoutingRule>();
		for (ClientRoutingRuleXml clientRoutingRuleXml 
				: this.clientRoutingRulesXml) {
			clientRoutingRules.add(clientRoutingRuleXml.toClientRoutingRule());
		}
		return ClientRoutingRules.newInstance(clientRoutingRules); 
	}
	
	@Override
	public Object toValue() {
		return this.toClientRoutingRules();
	}
	
}
