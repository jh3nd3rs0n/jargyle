package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.RuleResult;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "ruleResults", propOrder = { "ruleResultsXml" }) 
class RuleResultsXml {
	
	@XmlElement(name = "ruleResult")
	protected List<RuleResultXml> ruleResultsXml;

	public RuleResultsXml() {
		this.ruleResultsXml = new ArrayList<RuleResultXml>();
	}

	public RuleResultsXml(final List<RuleResult<? extends Object>> ruleResults) {
		List<RuleResultXml> rlResultsXml = new ArrayList<RuleResultXml>();
		for (RuleResult<? extends Object> ruleResult : ruleResults) {
			rlResultsXml.add(new RuleResultXml(ruleResult));
		}
		this.ruleResultsXml = rlResultsXml;
	}
	
	public List<RuleResult<Object>> toRuleResults() {
		List<RuleResult<Object>> ruleResults = new ArrayList<RuleResult<Object>>();
		for (RuleResultXml ruleResultXml : this.ruleResultsXml) {
			ruleResults.add(ruleResultXml.toRuleResult());
		}
		return Collections.unmodifiableList(ruleResults);
	}
	
}
