package jargyle.server.socks5;

import jargyle.common.security.HashedPassword;

public final class StringSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {

	private final Users users;
		
	public StringSourceUsernamePasswordAuthenticator(final String value) {
		super(value);
		this.users = Users.newInstance(value);
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
		return hashedPassword.equals(otherHashedPassword);
	}
	
}
