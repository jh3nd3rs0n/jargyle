package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ruleCondition", propOrder = { }) 
class RuleConditionXml {
	
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElement(name = "value", required = true)
	protected String value;	
	
	public RuleConditionXml() {
		this.name = null;
		this.value = null;
	}
	
	public RuleConditionXml(
			final RuleCondition<? extends Object, ? extends Object> ruleCondition) {
		this.name = ruleCondition.getName();
		this.value = ruleCondition.getValue().toString();
	}
	
	public RuleCondition<Object, Object> toRuleCondition() {
		return RuleCondition.newInstanceOfParsableValue(this.name, this.value);
	}

}
