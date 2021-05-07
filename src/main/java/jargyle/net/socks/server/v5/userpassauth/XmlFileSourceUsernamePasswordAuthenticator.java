package jargyle.net.socks.server.v5.userpassauth;

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
