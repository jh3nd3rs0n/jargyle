package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyRule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyRules;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5ReplyRules", propOrder = { "socks5ReplyRulesXml" }) 
class Socks5ReplyRulesXml extends ValueXml {
	
	@XmlElement(name = "socks5ReplyRule")
	protected List<Socks5ReplyRuleXml> socks5ReplyRulesXml;
	
	public Socks5ReplyRulesXml() {
		this.socks5ReplyRulesXml = new ArrayList<Socks5ReplyRuleXml>(); 
	}
	
	public Socks5ReplyRulesXml(final Socks5ReplyRules socks5ReplyRules) {
		this.socks5ReplyRulesXml = new ArrayList<Socks5ReplyRuleXml>();
		for (Socks5ReplyRule socks5ReplyRule : socks5ReplyRules.toList()) {
			this.socks5ReplyRulesXml.add(new Socks5ReplyRuleXml(
					socks5ReplyRule));
		}
	}
	
	public Socks5ReplyRules toSocks5ReplyRules() {
		List<Socks5ReplyRule> socks5ReplyRules = 
				new ArrayList<Socks5ReplyRule>();
		for (Socks5ReplyRuleXml socks5ReplyRuleXml : this.socks5ReplyRulesXml) {
			socks5ReplyRules.add(socks5ReplyRuleXml.toSocks5ReplyRule());
		}
		return Socks5ReplyRules.newInstance(socks5ReplyRules);
	}

	@Override
	public Object toValue() {
		return this.toSocks5ReplyRules();
	}

}
