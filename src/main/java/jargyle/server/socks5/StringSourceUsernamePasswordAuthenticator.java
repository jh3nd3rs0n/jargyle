package jargyle.server.socks5;

public final class StringSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {

	private final Users users;
		
	public StringSourceUsernamePasswordAuthenticator(
			final String paramString) {
		super(paramString);
		this.users = Users.newInstance(paramString);
	}

	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		if (this.users.toList().size() == 0) { return false; }
		User user = this.users.getLast(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		HashedPassword otherHashedPassword = HashedPassword.newInstance(
				password, hashedPassword);
		if (!hashedPassword.equals(otherHashedPassword)) { return false; }
		return true;
	}
	
}
