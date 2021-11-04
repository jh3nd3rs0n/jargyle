package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5ReplyRule", propOrder = { }) 
class Socks5ReplyRuleXml {

	@XmlElement(name = "ruleAction", required = true)
	protected String ruleAction;
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
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
	
	public Socks5ReplyRuleXml() {
		this.ruleAction = null;
		this.clientAddressRange = null;
		this.command = null;
		this.desiredDestinationAddressRange = null;
		this.desiredDestinationPortRange = null;
		this.serverBoundAddressRange = null;
		this.serverBoundPortRange = null;		
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5ReplyRuleXml(final Socks5ReplyRule socks5ReplyRule) {
		AddressRange clientAddrRange = 
				socks5ReplyRule.getClientAddressRange();
		Method meth = socks5ReplyRule.getMethod();
		String usr = socks5ReplyRule.getUser();
		Command cmd = socks5ReplyRule.getCommand();
		AddressRange desiredDestinationAddrRange =
				socks5ReplyRule.getDesiredDestinationAddressRange();
		PortRange desiredDestinationPrtRange = 
				socks5ReplyRule.getDesiredDestinationPortRange();
		AddressRange serverBoundAddrRange =
				socks5ReplyRule.getDesiredDestinationAddressRange();
		PortRange serverBoundPrtRange = 
				socks5ReplyRule.getDesiredDestinationPortRange();		
		LogAction lgAction = socks5ReplyRule.getLogAction();
		this.ruleAction = socks5ReplyRule.getRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ? 
				clientAddrRange.toString() : null;
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
		this.doc = socks5ReplyRule.getDoc();			
	}
	
	public Socks5ReplyRule toSocks5ReplyRule() {
		Socks5ReplyRule.Builder builder = new Socks5ReplyRule.Builder(
				RuleAction.valueOfString(this.ruleAction));
		if (this.clientAddressRange != null) {
			builder.clientAddressRange(AddressRange.newInstance(
					this.clientAddressRange));
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
