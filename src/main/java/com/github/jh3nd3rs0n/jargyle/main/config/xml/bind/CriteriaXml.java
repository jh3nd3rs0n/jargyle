package com.github.jh3nd3rs0n.jargyle.main.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.text.Criteria;
import com.github.jh3nd3rs0n.jargyle.common.text.Criterion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "criteria", propOrder = { "criteriaXml" }) 
class CriteriaXml extends ValueXml {
	
	@XmlElement(name = "criterion")
	protected List<CriterionXml> criteriaXml;
	
	public CriteriaXml() {
		this.criteriaXml = new ArrayList<CriterionXml>();
	}
	
	public CriteriaXml(final Criteria criteria) {
		this.criteriaXml = new ArrayList<CriterionXml>();
		for (Criterion criterion : criteria.toList()) {
			this.criteriaXml.add(new CriterionXml(criterion));
		}
	}
	
	public Criteria toCriteria() {
		List<Criterion> criteria = new ArrayList<Criterion>();
		for (CriterionXml criterionXml : this.criteriaXml) {
			criteria.add(criterionXml.toCriterion());
		}
		return Criteria.newInstance(criteria);
	}

	@Override
	public Object toValue() {
		return this.toCriteria();
	}
	
}