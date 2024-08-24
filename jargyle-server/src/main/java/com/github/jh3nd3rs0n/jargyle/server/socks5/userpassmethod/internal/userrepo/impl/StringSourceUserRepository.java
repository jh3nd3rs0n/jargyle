package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.internal.userrepo.impl;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.Users;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public final class StringSourceUserRepository extends UserRepository {

	private static User newUserFrom(final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"username password pair must be in the following format: "
							+ "USERNAME:PASSWORD");
		}
		String name;
		try {
			name = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String password;
		try {
			password = URLDecoder.decode(sElements[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return User.newInstance(name, password.toCharArray());
	}

	private static Users newUsersFrom(final String s) {
		List<User> users = new ArrayList<User>();
		if (s.isEmpty()) {
			return Users.of(users);
		}
		String[] sElements = s.split(",", -1);
		for (String sElement : sElements) {
			User user = newUserFrom(sElement);
			users.add(user);
		}
		return Users.of(users);
	}

	private final ReentrantLock lock;
	private final Users users;
	
	public StringSourceUserRepository(
			final UserRepositorySpec userRepositorySpec, 
			final String initializationStr) {
		super(userRepositorySpec, initializationStr);
		this.lock = new ReentrantLock();
		this.users = newUsersFrom(initializationStr);
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
			usrs = Users.of(this.users);
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
