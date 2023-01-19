package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.util.concurrent.locks.ReentrantLock;

public final class StringSourceUserRepository extends UserRepository {

	private final ReentrantLock lock;
	private final Users users;
	
	public StringSourceUserRepository(final String initializationVal) {
		super(initializationVal);
		this.lock = new ReentrantLock();
		this.users = Users.newInstance(initializationVal);
	}

	@Override
	public User get(final String name) {
		User user = null;
		this.lock.lock();
		try {
			user = this.users.get(name); 
		} finally {
			this.lock.unlock();
		}
		return user;
	}

	@Override
	public Users getAll() {
		Users usrs = null;
		this.lock.lock();
		try {
			usrs = Users.newInstance(this.users);
		} finally {
			this.lock.unlock();
		}
		return usrs;
	}

	@Override
	public void put(final User user) {
		this.lock.lock();
		try {
			this.users.put(user);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void putAll(final Users users) {
		this.lock.lock();
		try {
			this.users.putAll(users);
		} finally {
			this.lock.unlock();
		}
	}

	@Override
	public void remove(final String name) {
		this.lock.lock();
		try {
			this.users.remove(name);
		} finally {
			this.lock.unlock();
		}
	}

}
