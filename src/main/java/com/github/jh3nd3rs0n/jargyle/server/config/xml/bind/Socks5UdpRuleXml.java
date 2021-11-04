package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5UdpRule", propOrder = { }) 
class Socks5UdpRuleXml {

	@XmlElement(name = "ruleAction", required = true)
	protected String ruleAction;
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
	
	public Socks5UdpRuleXml() {
		this.ruleAction = null;
		this.clientAddressRange = null;
		this.peerAddressRange = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5UdpRuleXml(final Socks5UdpRule socks5UdpRule) {
		AddressRange clientAddrRange = socks5UdpRule.getClientAddressRange();
		Method meth = socks5UdpRule.getMethod();
		String usr = socks5UdpRule.getUser();
		AddressRange peerAddrRange = socks5UdpRule.getPeerAddressRange();
		LogAction lgAction = socks5UdpRule.getLogAction();
		this.ruleAction = socks5UdpRule.getRuleAction().toString();
		this.clientAddressRange = (clientAddrRange != null) ? 
				clientAddrRange.toString() : null;
		this.method = (meth != null) ? meth.toString() : null;
		this.user = usr;
		this.peerAddressRange =	(peerAddrRange != null) ? 
				peerAddrRange.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = socks5UdpRule.getDoc();			
	}
	
	public Socks5UdpRule toSocks5UdpRule() {
		Socks5UdpRule.Builder builder = new Socks5UdpRule.Builder(
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
