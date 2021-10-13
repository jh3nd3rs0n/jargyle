package com.github.jh3nd3rs0n.jargyle.main.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriterion;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestCriterion", propOrder = { }) 
class Socks5RequestCriterionXml {
	
	@XmlElement(name = "clientAddressCriterion")
	protected CriterionXml clientAddressCriterionXml;
	@XmlElement(name = "commandCriterion")
	protected CriterionXml commandCriterionXml;
	@XmlElement(name = "desiredDestinationAddressCriterion")
	protected CriterionXml desiredDestinationAddressCriterionXml;
	@XmlElement(name = "desiredDestinationPortRanges")
	protected PortRangesXml desiredDestinationPortRangesXml;
	@XmlElement(name = "doc")
	protected String doc;
	
	public Socks5RequestCriterionXml() {
		this.clientAddressCriterionXml = null;
		this.commandCriterionXml = null;
		this.desiredDestinationAddressCriterionXml = null;
		this.desiredDestinationPortRangesXml = null;
		this.doc = null;
	}
	
	public Socks5RequestCriterionXml(
			final Socks5RequestCriterion socks5RequestCriterion) {
		this.clientAddressCriterionXml = new CriterionXml(
				socks5RequestCriterion.getClientAddressCriterion());
		this.commandCriterionXml = new CriterionXml(
				socks5RequestCriterion.getCommandCriterion());
		this.desiredDestinationAddressCriterionXml = new CriterionXml(
				socks5RequestCriterion.getDesiredDestinationAddressCriterion());
		this.desiredDestinationPortRangesXml = new PortRangesXml(
				socks5RequestCriterion.getDesiredDestinationPortRanges());
		this.doc = socks5RequestCriterion.getDoc();			
	}
	
	public Socks5RequestCriterion toSocks5RequestCriterion() {
		return new Socks5RequestCriterion.Builder()
				.clientAddressCriterion(
						this.clientAddressCriterionXml.toCriterion())
				.commandCriterion(this.commandCriterionXml.toCriterion())
				.desiredDestinationAddressCriterion(
						this.desiredDestinationAddressCriterionXml.toCriterion())
				.desiredDestinationPortRanges(
						this.desiredDestinationPortRangesXml.toPortRanges())
				.doc(this.doc)
				.build();			
	}
}