package jargyle.net.socks.server.v5.userpassauth;

import jargyle.security.HashedPassword;

final class UsersSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
	
	private final UsersService usersService;
	
	public UsersSourceUsernamePasswordAuthenticator(
			final UsersService usrsService) {
		super(usrsService.getSource());
		this.usersService = usrsService;
	}

	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		Users users = this.usersService.getUsers();
		User user = users.get(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		HashedPassword otherHashedPassword = HashedPassword.newInstance(
				password, hashedPassword);
		return hashedPassword.equals(otherHashedPassword);
	}
	
}
