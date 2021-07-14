package jargyle.net.socks.server.v5.userpassauth;

final class UsersSourceUsernamePasswordAuthenticator 
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
		HashedPassword otherHashedPassword = HashedPassword.newInstance(
				password, hashedPassword);
		return hashedPassword.equals(otherHashedPassword);
	}
	
}
