package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

public final class StringSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
		
	private final UsersSourceUsernamePasswordAuthenticator authenticator;
	
	public StringSourceUsernamePasswordAuthenticator(final String string) {
		super(string);
		this.authenticator = new UsersSourceUsernamePasswordAuthenticator(
				new StringSourceUsersProvider(string));
	}
	
	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		return this.authenticator.authenticate(username, password);
	}
	
}
