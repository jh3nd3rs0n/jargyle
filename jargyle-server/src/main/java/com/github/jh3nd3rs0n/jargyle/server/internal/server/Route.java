package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.*;

public abstract class Route {

	private final String id;

	public Route(final String id) {
		this.id = id;
	}

	public abstract DatagramSocketFactory getDatagramSocketFactory();

	public abstract HostResolverFactory getHostResolverFactory();

	public final String getId() {
		return this.id;
	}

	public abstract ServerSocketFactory getServerSocketFactory();

	public abstract SocketFactory getSocketFactory();

	@Override
	public String toString() {
        return this.getClass().getSimpleName() +
                " [id=" +
                this.id +
                "]";
	}
	
}
