package com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.userpassauth;

abstract class UsersProvider {

	private final String source;
	
	public UsersProvider(final String src) { 
		this.source = src;
	}
	
	public final String getSource() {
		return this.source;
	}
	
	public abstract Users getUsers();
	
}
