package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

public final class Route {

	private final String id;
	private final NetObjectFactory netObjectFactory;
	
	public Route(final String id, final NetObjectFactory netObjFactory) {
		this.id = id;
		this.netObjectFactory = netObjFactory;
	}

	public NetObjectFactory getNetObjectFactory() {
		return this.netObjectFactory;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [id=")
			.append(this.id)
			.append(", netObjectFactory=")
			.append(this.netObjectFactory)
			.append("]");
		return builder.toString();
	}
	
}
