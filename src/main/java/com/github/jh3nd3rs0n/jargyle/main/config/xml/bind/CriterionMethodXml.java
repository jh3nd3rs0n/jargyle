package com.github.jh3nd3rs0n.jargyle.main.config.xml.bind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.text.CriterionMethod;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "criterionMethod")
@XmlEnum(String.class) 
enum CriterionMethodXml {

	@XmlEnumValue("equals")
	EQUALS(CriterionMethod.EQUALS),

	@XmlEnumValue("matches")
	MATCHES(CriterionMethod.MATCHES);

	public static CriterionMethodXml valueOfCriterionMethod(
			final CriterionMethod criterionMethod) {
		for (CriterionMethodXml value : CriterionMethodXml.values()) {
			if (value.criterionMethodValue().equals(criterionMethod)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<CriterionMethodXml> list = Arrays.asList(
				CriterionMethodXml.values());
		for (Iterator<CriterionMethodXml> iterator = list.iterator();
				iterator.hasNext();) {
			CriterionMethodXml value = iterator.next();
			CriterionMethod criterionMethodValue = 
					value.criterionMethodValue();
			sb.append(criterionMethodValue.toString());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(String.format(
				"expected criterion method must be one of "
				+ "the following values: %s. actual value is %s",
				sb.toString(),
				criterionMethod.toString()));
	}
	
	private final CriterionMethod criterionMethodValue;
	
	private CriterionMethodXml(final CriterionMethod methodValue) {
		this.criterionMethodValue = methodValue;
	}
	
	public CriterionMethod criterionMethodValue() {
		return this.criterionMethodValue;
	}
	
}