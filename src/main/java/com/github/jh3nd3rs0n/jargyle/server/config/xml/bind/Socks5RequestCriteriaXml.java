package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriteria;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriterion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestCriteria", propOrder = { "socks5RequestCriteriaXml" }) 
class Socks5RequestCriteriaXml extends ValueXml {
	
	@XmlElement(name = "socks5RequestCriterion")
	protected List<Socks5RequestCriterionXml> socks5RequestCriteriaXml;
	
	public Socks5RequestCriteriaXml() {
		this.socks5RequestCriteriaXml = new ArrayList<Socks5RequestCriterionXml>(); 
	}
	
	public Socks5RequestCriteriaXml(
			final Socks5RequestCriteria socks5RequestCriteria) {
		this.socks5RequestCriteriaXml = new ArrayList<Socks5RequestCriterionXml>();
		for (Socks5RequestCriterion socks5RequestCriterion 
				: socks5RequestCriteria.toList()) {
			this.socks5RequestCriteriaXml.add(new Socks5RequestCriterionXml(
					socks5RequestCriterion));
		}
	}
	
	public Socks5RequestCriteria toSocks5RequestCriteria() {
		List<Socks5RequestCriterion> socks5RequestCriteria = 
				new ArrayList<Socks5RequestCriterion>();
		for (Socks5RequestCriterionXml socks5RequestCriterionXml 
				: this.socks5RequestCriteriaXml) {
			socks5RequestCriteria.add(
					socks5RequestCriterionXml.toSocks5RequestCriterion());
		}
		return Socks5RequestCriteria.newInstance(socks5RequestCriteria);
	}

	@Override
	public Object toValue() {
		return this.toSocks5RequestCriteria();
	}
	
}