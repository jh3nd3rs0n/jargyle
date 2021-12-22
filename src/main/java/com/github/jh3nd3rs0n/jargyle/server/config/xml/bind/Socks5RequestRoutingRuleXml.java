package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRoutingRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5RequestRoutingRule", propOrder = { }) 
class Socks5RequestRoutingRuleXml {
	
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
	@XmlElement(name = "routeIds")
	protected RouteIdsXml routeIdsXml;
	@XmlElement(name = "routeIdSelectionStrategy")
	protected String routeIdSelectionStrategy;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;

	public Socks5RequestRoutingRuleXml() {
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.method = null;
		this.user = null;
		this.command = null;
		this.desiredDestinationAddressRange = null;
		this.desiredDestinationPortRange = null;
		this.routeIdsXml = null;
		this.routeIdSelectionStrategy = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public Socks5RequestRoutingRuleXml(
			final Socks5RequestRoutingRule socks5RequestRoutingRule) {
		AddressRange clientAddrRange =
				socks5RequestRoutingRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				socks5RequestRoutingRule.getSocksServerAddressRange();
		Method meth = socks5RequestRoutingRule.getMethod();
		String usr = socks5RequestRoutingRule.getUser();
		Command cmd = socks5RequestRoutingRule.getCommand();
		AddressRange desiredDestinationAddrRange =
				socks5RequestRoutingRule.getDesiredDestinationAddressRange();
		PortRange desiredDestinationPrtRange = 
				socks5RequestRoutingRule.getDesiredDestinationPortRange();
		List<String> ids = socks5RequestRoutingRule.getRouteIds();
		SelectionStrategy selectionStrategy = 
				socks5RequestRoutingRule.getRouteIdSelectionStrategy();
		LogAction lgAction = socks5RequestRoutingRule.getLogAction();
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
		this.routeIdsXml = (ids != null) ? new RouteIdsXml(ids) : null;
		this.routeIdSelectionStrategy = (selectionStrategy != null) ? 
				selectionStrategy.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = socks5RequestRoutingRule.getDoc();		
	}
	
	public Socks5RequestRoutingRule toSocks5RequestRoutingRule() {
		Socks5RequestRoutingRule.Builder builder = 
				new Socks5RequestRoutingRule.Builder();
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
		if (this.routeIdsXml != null) {
			List<String> routeIds = this.routeIdsXml.toRouteIds();
			for (String routeId : routeIds) {
				builder.addRouteId(routeId);
			}
		}
		if (this.routeIdSelectionStrategy != null) {
			builder.routeIdSelectionStrategy(SelectionStrategy.valueOfString(
					this.routeIdSelectionStrategy));
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
