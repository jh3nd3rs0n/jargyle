package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.ClientRule;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientRule", propOrder = { }) 
class ClientRuleXml {
	
	@XmlElement(name = "ruleAction", required = true)
	protected String ruleAction;
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
	@XmlElement(name = "socksServerAddressRange")
	protected String socksServerAddressRange;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;

	public ClientRuleXml() {
		this.ruleAction = null;
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public ClientRuleXml(final ClientRule clientRule) {
		AddressRange clientAddrRange = clientRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				clientRule.getSocksServerAddressRange();
		LogAction lgAction = clientRule.getLogAction();
		this.ruleAction = clientRule.getRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ?
				clientAddrRange.toString() : null;
		this.socksServerAddressRange = (socksServerAddrRange != null) ?
				socksServerAddrRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = clientRule.getDoc();
	}
	
	public ClientRule toClientRule() {
		ClientRule.Builder builder = new ClientRule.Builder(
				RuleAction.valueOfString(this.ruleAction));
		if (this.clientAddressRange != null) {
			builder.clientAddressRange(AddressRange.newInstance(
					this.clientAddressRange));
		}
		if (this.socksServerAddressRange != null) {
			builder.socksServerAddressRange(AddressRange.newInstance(
					this.socksServerAddressRange));
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
