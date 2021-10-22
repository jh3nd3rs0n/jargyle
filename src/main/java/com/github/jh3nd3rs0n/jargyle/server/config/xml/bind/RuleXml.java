package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.Rule;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "rule", propOrder = { }) 
class RuleXml {
	
	@XmlAttribute(name = "action", required = true)
	protected RuleActionXml ruleActionXml;
	@XmlElement(name = "conditionPredicate", required = true)
	protected ConditionPredicateXml conditionPredicateXml;
	@XmlElement(name = "doc")
	protected String doc;

	public RuleXml() {
		this.ruleActionXml = null;
		this.conditionPredicateXml = null;
		this.doc = null;
	}
	
	public RuleXml(final Rule rule) {
		this.ruleActionXml = RuleActionXml.valueOfRuleAction(
				rule.getRuleAction());
		this.conditionPredicateXml = new ConditionPredicateXml(
				rule.getConditionPredicate());
		this.doc = rule.getDoc();
	}
	
	public Rule toRule() {
		return Rule.newInstance(
				this.ruleActionXml.ruleActionValue(), 
				this.conditionPredicateXml.toConditionPredicate(), 
				this.doc);
	}
	
}
