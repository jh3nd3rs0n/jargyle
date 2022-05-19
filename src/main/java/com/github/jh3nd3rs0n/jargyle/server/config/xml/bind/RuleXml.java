package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "rule", propOrder = { }) 
class RuleXml extends ValueXml {

	@XmlElement(name = "ruleConditions")
	protected RuleConditionsXml ruleConditionsXml;
	@XmlElement(name = "ruleResults")
	protected RuleResultsXml ruleResultsXml;
	
	public RuleXml() {
		this.ruleConditionsXml = null;
		this.ruleResultsXml = null;
	}
	
	public RuleXml(final Rule rule) {
		List<RuleCondition<? extends Object, ? extends Object>> ruleConditions =
				new ArrayList<RuleCondition<? extends Object, ? extends Object>>(
						rule.getRuleConditions());
		List<RuleResult<? extends Object>> ruleResults = 
				new ArrayList<RuleResult<? extends Object>>(
						rule.getRuleResults());
		this.ruleConditionsXml = new RuleConditionsXml(ruleConditions);
		this.ruleResultsXml = new RuleResultsXml(ruleResults);
	}
	
	public Rule toRule() {
		Rule.Builder builder = new Rule.Builder();
		for (RuleCondition<Object, Object> ruleCondition 
				: this.ruleConditionsXml.toRuleConditions()) {
			builder.addRuleCondition(ruleCondition);
		}
		for (RuleResult<Object> ruleResult 
				: this.ruleResultsXml.toRuleResults()) {
			builder.addRuleResult(ruleResult);
		}
		return builder.build();
	}

	@Override
	public Object toValue() {
		return this.toRule();
	}

}
