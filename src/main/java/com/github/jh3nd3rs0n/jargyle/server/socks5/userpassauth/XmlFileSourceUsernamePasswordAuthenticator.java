package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

public final class XmlFileSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
	
	private final UsersSourceUsernamePasswordAuthenticator authenticator;
	
	public XmlFileSourceUsernamePasswordAuthenticator(final String xmlFile) {
		super(xmlFile);
		this.authenticator = new UsersSourceUsernamePasswordAuthenticator(
				XmlFileSourceUsersProvider.newInstance(xmlFile));
	}
	
	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		return this.authenticator.authenticate(username, password);
	}

}
