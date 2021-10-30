package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "rule", propOrder = { }) 
class RuleXml {
	
	@XmlElement(name = "action", required = true)
	protected String ruleAction;
	@XmlElement(name = "sourceAddressRange")
	protected String sourceAddressRange;
	@XmlElement(name = "destinationAddressRange")
	protected String destinationAddressRange;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;

	public RuleXml() {
		this.ruleAction = null;
		this.sourceAddressRange = null;
		this.destinationAddressRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public RuleXml(final Rule rule) {
		AddressRange sourceAddrRange = rule.getSourceAddressRange();
		AddressRange destinationAddrRange = rule.getDestinationAddressRange();
		LogAction lgAction = rule.getLogAction();
		this.ruleAction = rule.getRuleAction().toString();
		this.sourceAddressRange = (sourceAddrRange != null) ?
				sourceAddrRange.toString() : null;
		this.destinationAddressRange = (destinationAddrRange != null) ?
				destinationAddrRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = rule.getDoc();
	}
	
	public Rule toRule() {
		Rule.Builder builder = new Rule.Builder(RuleAction.valueOfString(
				this.ruleAction));
		if (this.sourceAddressRange != null) {
			builder.sourceAddressRange(AddressRange.newInstance(
					this.sourceAddressRange));
		}
		if (this.destinationAddressRange != null) {
			builder.destinationAddressRange(AddressRange.newInstance(
					this.destinationAddressRange));
		}
		if (this.logAction != null) {
			builder.logAction(LogAction.valueOfString(this.logAction));
		}
		if (this.doc != null) {
			builder.doc(this.doc);
		}
		return builder.build();
	}
	
}
