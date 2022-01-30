package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

public final class RouteNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String routeId;
	
	public RouteNotFoundException(final String id) {
		super(String.format("route '%s' not found", id));
		this.routeId = id;
	}
	
	public String getRouteId() {
		return this.routeId;
	}
	
}
