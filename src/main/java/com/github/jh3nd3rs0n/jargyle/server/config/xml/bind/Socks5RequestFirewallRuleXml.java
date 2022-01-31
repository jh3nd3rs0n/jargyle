package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5RequestFirewallRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestFirewallRule", propOrder = { }) 
class Socks5RequestFirewallRuleXml {

	@XmlElement(name = "firewallRuleAction", required = true)
	protected String firewallRuleAction;
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
	@XmlElement(name = "socksServerAddressRange")
	protected String socksServerAddressRange;
	@XmlElement(name = "method")
	protected String method;
	@XmlElement(name = "user")
	protected String user;
	@XmlElement(name = "command")
	protected String command;
	@XmlElement(name = "desiredDestinationAddressRange")
	protected String desiredDestinationAddressRange;
	@XmlElement(name = "desiredDestinationPortRange")
	protected String desiredDestinationPortRange;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;
	
	public Socks5RequestFirewallRuleXml() {
		this.firewallRuleAction = null;
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.command = null;
		this.desiredDestinationAddressRange = null;
		this.desiredDestinationPortRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5RequestFirewallRuleXml(
			final Socks5RequestFirewallRule socks5RequestFirewallRule) {
		AddressRange clientAddrRange = 
				socks5RequestFirewallRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				socks5RequestFirewallRule.getSocksServerAddressRange();		
		Method meth = socks5RequestFirewallRule.getMethod();
		String usr = socks5RequestFirewallRule.getUser();
		Command cmd = socks5RequestFirewallRule.getCommand();
		AddressRange desiredDestinationAddrRange =
				socks5RequestFirewallRule.getDesiredDestinationAddressRange();
		PortRange desiredDestinationPrtRange = 
				socks5RequestFirewallRule.getDesiredDestinationPortRange();
		LogAction lgAction = socks5RequestFirewallRule.getLogAction();
		this.firewallRuleAction = 
				socks5RequestFirewallRule.getFirewallRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ? 
				clientAddrRange.toString() : null;
		this.socksServerAddressRange = (socksServerAddrRange != null) ?
				socksServerAddrRange.toString() : null;
		this.method = (meth != null) ? meth.toString() : null;
		this.user = usr;
		this.command = (cmd != null) ? cmd.toString() : null;
		this.desiredDestinationAddressRange = 
				(desiredDestinationAddrRange != null) ? 
						desiredDestinationAddrRange.toString() : null;
		this.desiredDestinationPortRange = 
				(desiredDestinationPrtRange != null) ?
						desiredDestinationPrtRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = socks5RequestFirewallRule.getDoc();			
	}
	
	public Socks5RequestFirewallRule toSocks5RequestFirewallRule() {
		Socks5RequestFirewallRule.Builder builder = 
				new Socks5RequestFirewallRule.Builder(
						FirewallRuleAction.valueOfString(
								this.firewallRuleAction));
		if (this.clientAddressRange != null) {
			builder.clientAddressRange(AddressRange.newInstance(
					this.clientAddressRange));
		}
		if (this.socksServerAddressRange != null) {
			builder.socksServerAddressRange(AddressRange.newInstance(
					this.socksServerAddressRange));
		}
		if (this.method != null) {
			builder.method(Method.valueOfString(this.method));
		}
		if (this.user != null) {
			builder.user(this.user);
		}
		if (this.command != null) {
			builder.command(Command.valueOfString(this.command));
		}
		if (this.desiredDestinationAddressRange != null) {
			builder.desiredDestinationAddressRange(AddressRange.newInstance(
					this.desiredDestinationAddressRange));
		}
		if (this.desiredDestinationPortRange != null) {
			builder.desiredDestinationPortRange(PortRange.newInstance(
					this.desiredDestinationPortRange));
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
