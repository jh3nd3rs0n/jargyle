package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5ReplyFirewallRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5ReplyFirewallRule", propOrder = { }) 
class Socks5ReplyFirewallRuleXml {

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
	@XmlElement(name = "serverBoundAddressRange")
	protected String serverBoundAddressRange;
	@XmlElement(name = "serverBoundPortRange")
	protected String serverBoundPortRange;	
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;
	
	public Socks5ReplyFirewallRuleXml() {
		this.firewallRuleAction = null;
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.command = null;
		this.desiredDestinationAddressRange = null;
		this.desiredDestinationPortRange = null;
		this.serverBoundAddressRange = null;
		this.serverBoundPortRange = null;		
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5ReplyFirewallRuleXml(
			final Socks5ReplyFirewallRule socks5ReplyFirewallRule) {
		AddressRange clientAddrRange = 
				socks5ReplyFirewallRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				socks5ReplyFirewallRule.getSocksServerAddressRange();
		Method meth = socks5ReplyFirewallRule.getMethod();
		String usr = socks5ReplyFirewallRule.getUser();
		Command cmd = socks5ReplyFirewallRule.getCommand();
		AddressRange desiredDestinationAddrRange =
				socks5ReplyFirewallRule.getDesiredDestinationAddressRange();
		PortRange desiredDestinationPrtRange = 
				socks5ReplyFirewallRule.getDesiredDestinationPortRange();
		AddressRange serverBoundAddrRange =
				socks5ReplyFirewallRule.getDesiredDestinationAddressRange();
		PortRange serverBoundPrtRange = 
				socks5ReplyFirewallRule.getDesiredDestinationPortRange();		
		LogAction lgAction = socks5ReplyFirewallRule.getLogAction();
		this.firewallRuleAction = 
				socks5ReplyFirewallRule.getFirewallRuleAction().toString();
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
		this.serverBoundAddressRange = (serverBoundAddrRange != null) ? 
				serverBoundAddrRange.toString() : null;
		this.serverBoundPortRange =	(serverBoundPrtRange != null) ?
				serverBoundPrtRange.toString() : null;		
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = socks5ReplyFirewallRule.getDoc();			
	}
	
	public Socks5ReplyFirewallRule toSocks5ReplyFirewallRule() {
		Socks5ReplyFirewallRule.Builder builder = 
				new Socks5ReplyFirewallRule.Builder(
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
		if (this.serverBoundAddressRange != null) {
			builder.serverBoundAddressRange(AddressRange.newInstance(
					this.serverBoundAddressRange));
		}
		if (this.serverBoundPortRange != null) {
			builder.serverBoundPortRange(PortRange.newInstance(
					this.serverBoundPortRange));
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
