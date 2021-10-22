package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Rules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "rules", propOrder = { "rulesXml" }) 
class RulesXml extends ValueXml {
	
	@XmlElement(name = "rule")
	protected List<RuleXml> rulesXml;
	
	public RulesXml() {
		this.rulesXml = new ArrayList<RuleXml>();
	}
	
	public RulesXml(final Rules rules) {
		this.rulesXml = new ArrayList<RuleXml>();
		for (Rule rule : rules.toList()) {
			this.rulesXml.add(new RuleXml(rule));
		}
	}
	
	public Rules toRules() {
		List<Rule> rules = new ArrayList<Rule>();
		for (RuleXml ruleXml : this.rulesXml) {
			rules.add(ruleXml.toRule());
		}
		return Rules.newInstance(rules);
	}

	@Override
	public Object toValue() {
		return this.toRules();
	}

}
