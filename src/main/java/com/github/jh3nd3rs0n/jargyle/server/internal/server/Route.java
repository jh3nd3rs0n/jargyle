package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

public final class Route {

	private final String routeId;
	private final NetObjectFactory netObjectFactory;
	
	public Route(final String id, final NetObjectFactory netObjFactory) {
		this.routeId = id;
		this.netObjectFactory = netObjFactory;
	}

	public NetObjectFactory getNetObjectFactory() {
		return this.netObjectFactory;
	}
	
	public String getRouteId() {
		return this.routeId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [routeId=")
			.append(this.routeId)
			.append(", netObjectFactory=")
			.append(this.netObjectFactory)
			.append("]");
		return builder.toString();
	}
	
}
