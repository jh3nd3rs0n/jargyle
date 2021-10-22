package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.ConditionPredicateMethod;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "conditionPredicateMethod")
@XmlEnum(String.class) 
enum ConditionPredicateMethodXml {

	@XmlEnumValue("equals")
	EQUALS(ConditionPredicateMethod.EQUALS),

	@XmlEnumValue("matches")
	MATCHES(ConditionPredicateMethod.MATCHES);

	public static ConditionPredicateMethodXml valueOfConditionPredicateMethod(
			final ConditionPredicateMethod conditionPredicateMethod) {
		for (ConditionPredicateMethodXml value 
				: ConditionPredicateMethodXml.values()) {
			if (value.conditionPredicateMethodValue().equals(
					conditionPredicateMethod)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<ConditionPredicateMethodXml> list = Arrays.asList(
				ConditionPredicateMethodXml.values());
		for (Iterator<ConditionPredicateMethodXml> iterator = list.iterator();
				iterator.hasNext();) {
			ConditionPredicateMethodXml value = iterator.next();
			ConditionPredicateMethod conditionPredicateMethodValue = 
					value.conditionPredicateMethodValue();
			sb.append(conditionPredicateMethodValue.toString());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected condition predicate method must be one of the "
				+ "following values: %s. actual value is %s",
				sb.toString(),
				conditionPredicateMethod.toString()));
	}
	
	private final ConditionPredicateMethod conditionPredicateMethodValue;
	
	private ConditionPredicateMethodXml(
			final ConditionPredicateMethod condPredicateMethodValue) {
		this.conditionPredicateMethodValue = condPredicateMethodValue;
	}
	
	public ConditionPredicateMethod conditionPredicateMethodValue() {
		return this.conditionPredicateMethodValue;
	}

}
