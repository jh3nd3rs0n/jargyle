package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleCondition;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "rule", propOrder = { }) 
class RuleXml extends ValueXml {

	@XmlElement(name = "ruleConditions")
	protected RuleConditionsXml ruleConditionsXml;
	@XmlElement(name = "ruleActions")
	protected RuleActionsXml ruleActionsXml;
	
	public RuleXml() {
		this.ruleConditionsXml = null;
		this.ruleActionsXml = null;
	}
	
	public RuleXml(final Rule rule) {
		List<RuleCondition<? extends Object, ? extends Object>> ruleConditions =
				new ArrayList<RuleCondition<? extends Object, ? extends Object>>(
						rule.getRuleConditions());
		List<RuleAction<? extends Object>> ruleActions = 
				new ArrayList<RuleAction<? extends Object>>(
						rule.getRuleActions());
		this.ruleConditionsXml = new RuleConditionsXml(ruleConditions);
		this.ruleActionsXml = new RuleActionsXml(ruleActions);
	}
	
	public Rule toRule() {
		Rule.Builder builder = new Rule.Builder();
		for (RuleCondition<Object, Object> ruleCondition 
				: this.ruleConditionsXml.toRuleConditions()) {
			builder.addRuleCondition(ruleCondition);
		}
		for (RuleAction<Object> ruleAction 
				: this.ruleActionsXml.toRuleActions()) {
			builder.addRuleAction(ruleAction);
		}
		return builder.build();
	}

	@Override
	public Object toValue() {
		return this.toRule();
	}

}
