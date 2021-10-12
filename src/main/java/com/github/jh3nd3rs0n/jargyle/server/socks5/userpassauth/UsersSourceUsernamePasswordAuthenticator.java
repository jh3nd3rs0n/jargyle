package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

public final class UsersSourceUsernamePasswordAuthenticator 
	extends UsernamePasswordAuthenticator {
	
	private final UsersProvider usersProvider;
	
	public UsersSourceUsernamePasswordAuthenticator(
			final UsersProvider usrsProvider) {
		super(usrsProvider.getSource());
		this.usersProvider = usrsProvider;
	}

	@Override
	public boolean authenticate(
			final String username, final char[] password) {
		Users users = this.usersProvider.getUsers();
		User user = users.get(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		return hashedPassword.passwordEquals(password);
	}
	
}
