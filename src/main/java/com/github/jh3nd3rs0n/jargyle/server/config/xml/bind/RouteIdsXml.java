package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "routeIds", propOrder = { "routeIds" }) 
class RouteIdsXml {

	@XmlElement(name = "routeId")
	protected List<String> routeIds;
	
	public RouteIdsXml() {
		this.routeIds = new ArrayList<String>();
	}

	public RouteIdsXml(final List<String> ids) {
		this.routeIds = new ArrayList<String>(ids);
	}
	
	public List<String> toRouteIds() {
		return Collections.unmodifiableList(this.routeIds);
	}
	
}
