package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRule;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestRule", propOrder = { }) 
class Socks5RequestRuleXml {

	@XmlAttribute(name = "action", required = true)
	protected ActionXml actionXml;
	@XmlElement(name = "clientAddressConditionPredicate")
	protected ConditionPredicateXml clientAddressConditionPredicateXml;
	@XmlElement(name = "commandConditionPredicate")
	protected ConditionPredicateXml commandConditionPredicateXml;
	@XmlElement(name = "desiredDestinationAddressConditionPredicate")
	protected ConditionPredicateXml desiredDestinationAddressConditionPredicateXml;
	@XmlElement(name = "desiredDestinationPortRanges")
	protected PortRangesXml desiredDestinationPortRangesXml;
	@XmlElement(name = "doc")
	protected String doc;
	
	public Socks5RequestRuleXml() {
		this.actionXml = null;
		this.clientAddressConditionPredicateXml = null;
		this.commandConditionPredicateXml = null;
		this.desiredDestinationAddressConditionPredicateXml = null;
		this.desiredDestinationPortRangesXml = null;
		this.doc = null;
	}
	
	public Socks5RequestRuleXml(final Socks5RequestRule socks5RequestRule) {
		this.actionXml = ActionXml.valueOfAction(socks5RequestRule.getAction());
		this.clientAddressConditionPredicateXml = new ConditionPredicateXml(
				socks5RequestRule.getClientAddressConditionPredicate());
		this.commandConditionPredicateXml = new ConditionPredicateXml(
				socks5RequestRule.getCommandConditionPredicate());
		this.desiredDestinationAddressConditionPredicateXml = new ConditionPredicateXml(
				socks5RequestRule.getDesiredDestinationAddressConditionPredicate());
		this.desiredDestinationPortRangesXml = new PortRangesXml(
				socks5RequestRule.getDesiredDestinationPortRanges());
		this.doc = socks5RequestRule.getDoc();			
	}
	
	public Socks5RequestRule toSocks5RequestRule() {
		return new Socks5RequestRule.Builder(this.actionXml.actionValue())
				.clientAddressConditionPredicate(
						this.clientAddressConditionPredicateXml.toConditionPredicate())
				.commandConditionPredicate(
						this.commandConditionPredicateXml.toConditionPredicate())
				.desiredDestinationAddressConditionPredicate(
						this.desiredDestinationAddressConditionPredicateXml.toConditionPredicate())
				.desiredDestinationPortRanges(
						this.desiredDestinationPortRangesXml.toPortRanges())
				.doc(this.doc)
				.build();			
	}

}
