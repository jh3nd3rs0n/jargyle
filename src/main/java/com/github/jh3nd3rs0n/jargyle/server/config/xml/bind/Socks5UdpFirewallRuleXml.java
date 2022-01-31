package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5UdpFirewallRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5UdpFirewallRule", propOrder = { }) 
class Socks5UdpFirewallRuleXml {

	@XmlElement(name = "firewallRuleAction", required = true)
	protected String firewallRuleAction;
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
	@XmlElement(name = "method")
	protected String method;
	@XmlElement(name = "user")
	protected String user;
	@XmlElement(name = "peerAddressRange")
	protected String peerAddressRange;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;
	
	public Socks5UdpFirewallRuleXml() {
		this.firewallRuleAction = null;
		this.clientAddressRange = null;
		this.peerAddressRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5UdpFirewallRuleXml(
			final Socks5UdpFirewallRule socks5UdpFirewallRule) {
		AddressRange clientAddrRange = 
				socks5UdpFirewallRule.getClientAddressRange();
		Method meth = socks5UdpFirewallRule.getMethod();
		String usr = socks5UdpFirewallRule.getUser();
		AddressRange peerAddrRange = 
				socks5UdpFirewallRule.getPeerAddressRange();
		LogAction lgAction = socks5UdpFirewallRule.getLogAction();
		this.firewallRuleAction = 
				socks5UdpFirewallRule.getFirewallRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ? 
				clientAddrRange.toString() : null;
		this.method = (meth != null) ? meth.toString() : null;
		this.user = usr;
		this.peerAddressRange =	(peerAddrRange != null) ? 
				peerAddrRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = socks5UdpFirewallRule.getDoc();			
	}
	
	public Socks5UdpFirewallRule toSocks5UdpFirewallRule() {
		Socks5UdpFirewallRule.Builder builder = 
				new Socks5UdpFirewallRule.Builder(
						FirewallRuleAction.valueOfString(
								this.firewallRuleAction));
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
		if (this.peerAddressRange != null) {
			builder.peerAddressRange(AddressRange.newInstance(
					this.peerAddressRange));
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
