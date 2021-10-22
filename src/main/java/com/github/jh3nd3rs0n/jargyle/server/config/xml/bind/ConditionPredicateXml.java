package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.ConditionPredicate;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "conditionPredicate", propOrder = { }) 
class ConditionPredicateXml {
	
	@XmlAttribute(name = "method", required = true)
	protected ConditionPredicateMethodXml conditionPredicateMethodXml;
	@XmlAttribute(name = "value", required = true)
	protected String value;

	public ConditionPredicateXml() {
		this.conditionPredicateMethodXml = null;
		this.value = null;
	}
	
	public ConditionPredicateXml(final ConditionPredicate conditionPredicate) {
		this.conditionPredicateMethodXml = 
				ConditionPredicateMethodXml.valueOfConditionPredicateMethod(
						conditionPredicate.getConditionPredicateMethod());
		this.value = conditionPredicate.getValue();
	}
	
	public ConditionPredicate toConditionPredicate() {
		return ConditionPredicate.newInstance(
				this.conditionPredicateMethodXml.conditionPredicateMethodValue(), 
				this.value); 
	}

}
