package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestRule", propOrder = { }) 
class Socks5RequestRuleXml {

	@XmlElement(name = "ruleAction", required = true)
	protected String ruleAction;
	@XmlElement(name = "sourceAddressRange")
	protected String sourceAddressRange;
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
	
	public Socks5RequestRuleXml() {
		this.ruleAction = null;
		this.sourceAddressRange = null;
		this.command = null;
		this.desiredDestinationAddressRange = null;
		this.desiredDestinationPortRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5RequestRuleXml(final Socks5RequestRule socks5RequestRule) {
		AddressRange sourceAddrRange = 
				socks5RequestRule.getSourceAddressRange();
		Method meth = socks5RequestRule.getMethod();
		String usr = socks5RequestRule.getUser();
		Command cmd = socks5RequestRule.getCommand();
		AddressRange desiredDestinationAddrRange =
				socks5RequestRule.getDesiredDestinationAddressRange();
		PortRange desiredDestinationPrtRange = 
				socks5RequestRule.getDesiredDestinationPortRange();
		LogAction lgAction = socks5RequestRule.getLogAction();
		this.ruleAction = socks5RequestRule.getRuleAction().toString();
		this.sourceAddressRange = (sourceAddrRange != null) ? 
				sourceAddrRange.toString() : null;
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
		this.doc = socks5RequestRule.getDoc();			
	}
	
	public Socks5RequestRule toSocks5RequestRule() {
		Socks5RequestRule.Builder builder = new Socks5RequestRule.Builder(
				RuleAction.valueOfString(this.ruleAction));
		if (this.sourceAddressRange != null) {
			builder.sourceAddressRange(AddressRange.newInstance(
					this.sourceAddressRange));
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
