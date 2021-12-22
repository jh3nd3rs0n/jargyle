package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.ClientFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRuleAction;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientFirewallRule", propOrder = { }) 
class ClientFirewallRuleXml {
	
	@XmlElement(name = "firewallRuleAction", required = true)
	protected String firewallRuleAction;
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
	@XmlElement(name = "socksServerAddressRange")
	protected String socksServerAddressRange;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;

	public ClientFirewallRuleXml() {
		this.firewallRuleAction = null;
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public ClientFirewallRuleXml(final ClientFirewallRule clientFirewallRule) {
		AddressRange clientAddrRange = 
				clientFirewallRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				clientFirewallRule.getSocksServerAddressRange();
		LogAction lgAction = clientFirewallRule.getLogAction();
		this.firewallRuleAction = 
				clientFirewallRule.getFirewallRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ?
				clientAddrRange.toString() : null;
		this.socksServerAddressRange = (socksServerAddrRange != null) ?
				socksServerAddrRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = clientFirewallRule.getDoc();
	}
	
	public ClientFirewallRule toClientFirewallRule() {
		ClientFirewallRule.Builder builder = new ClientFirewallRule.Builder(
				FirewallRuleAction.valueOfString(this.firewallRuleAction));
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
