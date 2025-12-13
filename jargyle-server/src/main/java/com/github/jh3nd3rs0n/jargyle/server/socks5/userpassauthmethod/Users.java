package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

import java.util.*;

public final class Users {
	
	public static Users of(final List<User> users) {
		return new Users(users);
	}
	
	public static Users of(final User... users) {
		return of(Arrays.asList(users));
	}
	
	public static Users of(final Users users) {
		return new Users(users);
	}

	private final Map<String, User> users;
	
	private Users(final List<User> usrs) {
		Map<String, User> map = new LinkedHashMap<String, User>();
		for (User usr : usrs) {
			String name = usr.getName();
			if (map.containsKey(name)) {
				map.remove(name);
			}
			map.put(name, usr);
		}
		this.users = map;
	}
	
	private Users(final Users other) {
		this.users = new LinkedHashMap<String, User>(other.users);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Users other = (Users) obj;
		if (this.users == null) {
			if (other.users != null) {
				return false;
			}
		} else if (!this.users.equals(other.users)) {
			return false;
		}
		return true;
	}
	
	public User get(final String name) {
		return this.users.get(name);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.users == null) ? 
				0 : this.users.hashCode());
		return result;
	}

	public User put(final User usr) {
		String name = usr.getName();
		User recentUsr = this.remove(name);
		this.users.put(name, usr);
		return recentUsr;
	}
	
	public void putAll(final Users usrs) {
		for (User usr : usrs.users.values()) {
			this.put(usr);
		}
	}
	
	public User remove(final String name) {
		return this.users.remove(name);
	}

	public Map<String, User> toMap() {
		return Collections.unmodifiableMap(this.users);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
				.append(" [users=")
				.append(this.users)
				.append("]");
		return builder.toString();
	}	
}
