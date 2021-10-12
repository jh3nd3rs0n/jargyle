package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.text.Criterion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "criterion", propOrder = { }) 
class CriterionXml {
	
	@XmlAttribute(name = "method", required = true)
	protected CriterionMethodXml methodXml;
	@XmlAttribute(name = "value", required = true)
	protected String value;
	@XmlAttribute(name = "comment")
	protected String comment;

	public CriterionXml() {
		this.methodXml = null;
		this.value = null;
		this.comment = null;
	}
	
	public CriterionXml(final Criterion criterion) {
		this.methodXml = CriterionMethodXml.valueOfCriterionMethod(
				criterion.getCriterionMethod());
		this.value = criterion.getValue();
		this.comment = criterion.getComment();
	}
	
	public Criterion toCriterion() {
		return Criterion.newInstance(
				this.methodXml.criterionMethodValue(), 
				this.value, 
				this.comment); 
	}
	
}