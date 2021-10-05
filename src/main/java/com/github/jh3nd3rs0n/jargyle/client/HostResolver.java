package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.net.InetAddress;

public class HostResolver {
	
	public HostResolver() { }
	
	public InetAddress resolve(final String host) throws IOException {
		return InetAddress.getByName(host);
	}
	
}
