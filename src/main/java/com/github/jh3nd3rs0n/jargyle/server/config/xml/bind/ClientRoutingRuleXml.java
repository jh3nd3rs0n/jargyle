package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.ClientRoutingRule;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "clientRoutingRule", propOrder = { }) 
class ClientRoutingRuleXml {
	
	@XmlElement(name = "clientAddressRange")
	protected String clientAddressRange;
	@XmlElement(name = "socksServerAddressRange")
	protected String socksServerAddressRange;
	@XmlElement(name = "routeIds")
	protected RouteIdsXml routeIdsXml;
	@XmlElement(name = "routeIdSelectionStrategy")
	protected String routeIdSelectionStrategy;
	@XmlElement(name = "logAction")
	protected String logAction;
	@XmlElement(name = "doc")
	protected String doc;

	public ClientRoutingRuleXml() {
		this.clientAddressRange = null;
		this.socksServerAddressRange = null;
		this.routeIdsXml = null;
		this.routeIdSelectionStrategy = null;
		this.logAction = null;
		this.doc = null;
	}
	
	public ClientRoutingRuleXml(final ClientRoutingRule clientRoutingRule) {
		AddressRange clientAddrRange =
				clientRoutingRule.getClientAddressRange();
		AddressRange socksServerAddrRange = 
				clientRoutingRule.getSocksServerAddressRange();
		List<String> ids = clientRoutingRule.getRouteIds();
		SelectionStrategy selectionStrategy = 
				clientRoutingRule.getRouteIdSelectionStrategy();
		LogAction lgAction = clientRoutingRule.getLogAction();
		this.clientAddressRange = (clientAddrRange != null) ? 
				clientAddrRange.toString() : null;
		this.socksServerAddressRange = (socksServerAddrRange != null) ?
				socksServerAddrRange.toString() : null;		
		this.routeIdsXml = (ids != null) ? new RouteIdsXml(ids) : null;
		this.routeIdSelectionStrategy = (selectionStrategy != null) ? 
				selectionStrategy.toString() : null;
		this.logAction = (lgAction != null) ? lgAction.toString() : null;
		this.doc = clientRoutingRule.getDoc();		
	}
	
	public ClientRoutingRule toClientRoutingRule() {
		ClientRoutingRule.Builder builder = new ClientRoutingRule.Builder();
		if (this.clientAddressRange != null) {
			builder.clientAddressRange(AddressRange.newInstance(
					this.clientAddressRange));
		}
		if (this.socksServerAddressRange != null) {
			builder.socksServerAddressRange(AddressRange.newInstance(
					this.socksServerAddressRange));
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
