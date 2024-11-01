package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ruleActions", propOrder = { "ruleActionsXml" }) 
class RuleActionsXml {
	
	@XmlElement(name = "ruleAction")
	protected List<RuleActionXml> ruleActionsXml;

	public RuleActionsXml() {
		this.ruleActionsXml = new ArrayList<RuleActionXml>();
	}

	public RuleActionsXml(final List<RuleAction<? extends Object>> ruleActions) {
		List<RuleActionXml> rlResultsXml = new ArrayList<RuleActionXml>();
		for (RuleAction<? extends Object> ruleAction : ruleActions) {
			rlResultsXml.add(new RuleActionXml(ruleAction));
		}
		this.ruleActionsXml = rlResultsXml;
	}
	
	public List<RuleAction<Object>> toRuleActions() {
		List<RuleAction<Object>> ruleActions = new ArrayList<RuleAction<Object>>();
		for (RuleActionXml ruleActionXml : this.ruleActionsXml) {
			ruleActions.add(ruleActionXml.toRuleAction());
		}
		return Collections.unmodifiableList(ruleActions);
	}
	
}
