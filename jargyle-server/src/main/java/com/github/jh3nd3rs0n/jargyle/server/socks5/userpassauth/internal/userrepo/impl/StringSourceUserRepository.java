package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.userrepo.impl;

import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepositorySpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

public final class StringSourceUserRepository extends UserRepository {

	private final ReentrantLock lock;
	private final Users users;
	
	public StringSourceUserRepository(
			final UserRepositorySpec userRepositorySpec, 
			final String initializationStr) {
		super(userRepositorySpec, initializationStr);
		this.lock = new ReentrantLock();
		this.users = Users.newInstanceOfStringContainingPlaintextPasswords(
				initializationStr);
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
