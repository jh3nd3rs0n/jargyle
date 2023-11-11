package com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.config.xml.bind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ruleConditions", propOrder = { "ruleConditionsXml" }) 
class RuleConditionsXml {
	
	@XmlElement(name = "ruleCondition")
	protected List<RuleConditionXml> ruleConditionsXml;

	public RuleConditionsXml() {
		this.ruleConditionsXml = new ArrayList<RuleConditionXml>();
	}
	
	public RuleConditionsXml(
			final List<RuleCondition<? extends Object, ? extends Object>> ruleConditions) {
		List<RuleConditionXml> rlConditionsXml = 
				new ArrayList<RuleConditionXml>();
		for (RuleCondition<? extends Object, ? extends Object> ruleCondition 
				: ruleConditions) {
			rlConditionsXml.add(new RuleConditionXml(ruleCondition));
		}
		this.ruleConditionsXml = rlConditionsXml;
	}
	
	public List<RuleCondition<Object, Object>> toRuleConditions() {
		List<RuleCondition<Object, Object>> ruleConditions = 
				new ArrayList<RuleCondition<Object, Object>>();
		for (RuleConditionXml ruleConditionXml : this.ruleConditionsXml) {
			ruleConditions.add(ruleConditionXml.toRuleCondition());
		}
		return Collections.unmodifiableList(ruleConditions);
	}

}
