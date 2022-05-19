package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

public final class UsernamePasswordAuthenticator {
	
	private final UserRepository userRepository;
	
	public UsernamePasswordAuthenticator(final UserRepository repository) {
		this.userRepository = repository;
	}
	
	public boolean authenticate(
			final String username, final char[] password) {
		User user = this.userRepository.get(username);
		if (user == null) { return false; }
		HashedPassword hashedPassword = user.getHashedPassword();
		return hashedPassword.passwordEquals(password);
	}
	
}
