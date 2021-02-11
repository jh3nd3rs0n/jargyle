package jargyle.net.socks5;

import jargyle.security.HashedPassword;

public final class StringSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {

	private final UsersService usersService;
		
	public StringSourceUsernamePasswordAuthenticator(final String value) {
		super(value);
		this.usersService = new StringSourceUsersService(value);
	}

	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		Users users = this.usersService.getUsers();
		if (users.toList().size() == 0) { return false; }
		User user = users.getLast(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		HashedPassword otherHashedPassword = HashedPassword.newInstance(
				password, hashedPassword);
		return hashedPassword.equals(otherHashedPassword);
	}
	
}
