package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.RuleAction;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "ruleAction")
@XmlEnum(String.class) 
enum RuleActionXml {

	@XmlEnumValue("allow")
	ALLOW(RuleAction.ALLOW),

	@XmlEnumValue("block")
	BLOCK(RuleAction.BLOCK);

	public static RuleActionXml valueOfRuleAction(final RuleAction ruleAction) {
		for (RuleActionXml value : RuleActionXml.values()) {
			if (value.ruleActionValue().equals(ruleAction)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<RuleActionXml> list = Arrays.asList(RuleActionXml.values());
		for (Iterator<RuleActionXml> iterator = list.iterator();
				iterator.hasNext();) {
			RuleActionXml value = iterator.next();
			RuleAction ruleActionValue = value.ruleActionValue();
			sb.append(ruleActionValue.toString());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected rule action must be one of the following values: %s. "
				+ "actual value is %s",
				sb.toString(),
				ruleAction.toString()));
	}
	
	private final RuleAction ruleActionValue;
	
	private RuleActionXml(final RuleAction rlActionValue) {
		this.ruleActionValue = rlActionValue;
	}
	
	public RuleAction ruleActionValue() {
		return this.ruleActionValue;
	}

}
