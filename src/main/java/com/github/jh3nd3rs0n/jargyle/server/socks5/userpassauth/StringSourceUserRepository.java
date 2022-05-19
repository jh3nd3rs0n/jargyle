package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

public final class StringSourceUserRepository extends UserRepository {

	private final Users users;
	
	public StringSourceUserRepository(final String initializationVal) {
		super(initializationVal);
		this.users = Users.newInstance(initializationVal);
	}

	@Override
	public User get(final String name) {
		return this.users.get(name);
	}

	@Override
	public Users getAll() {
		return Users.newInstance(users);
	}

	@Override
	public void put(final User user) {
		this.users.put(user);
	}

	@Override
	public void putAll(final Users users) {
		this.users.putAll(users);
	}

	@Override
	public void remove(final String name) {
		this.users.remove(name);
	}

}
