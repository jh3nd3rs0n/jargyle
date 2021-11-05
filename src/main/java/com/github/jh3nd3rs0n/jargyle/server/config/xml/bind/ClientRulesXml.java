package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.ClientRule;
import com.github.jh3nd3rs0n.jargyle.server.ClientRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientRules", propOrder = { "clientRulesXml" }) 
class ClientRulesXml extends ValueXml {
	
	@XmlElement(name = "clientRule")
	protected List<ClientRuleXml> clientRulesXml;
	
	public ClientRulesXml() {
		this.clientRulesXml = new ArrayList<ClientRuleXml>();
	}
	
	public ClientRulesXml(final ClientRules clientRules) {
		this.clientRulesXml = new ArrayList<ClientRuleXml>();
		for (ClientRule clientRule : clientRules.toList()) {
			this.clientRulesXml.add(new ClientRuleXml(clientRule));
		}
	}
	
	public ClientRules toClientRules() {
		List<ClientRule> clientRules = new ArrayList<ClientRule>();
		for (ClientRuleXml clientRuleXml : this.clientRulesXml) {
			clientRules.add(clientRuleXml.toClientRule());
		}
		return ClientRules.newInstance(clientRules);
	}

	@Override
	public Object toValue() {
		return this.toClientRules();
	}

}
