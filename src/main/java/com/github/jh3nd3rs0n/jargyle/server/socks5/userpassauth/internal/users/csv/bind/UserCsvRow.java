package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

final class UserCsvRow {
	
	public static UserCsvRow newInstance(final User user) {
		String name = user.getName();
		if (name.indexOf(',') > -1 || name.indexOf('\n') > -1) {
			throw new IllegalArgumentException(
					"name must not contain any commas (,) "
					+ "or any newline characters (\\n)");
		}
		HashedPasswordValue hashedPasswordValue = 
				HashedPasswordValue.newInstance(user.getHashedPassword());
		List<String> values = new ArrayList<String>();
		values.add(name);
		values.add(hashedPasswordValue.toString());
		return new UserCsvRow(user, values);
	}
	
	public static UserCsvRow newInstanceFrom(
			final CsvParser csvParser) throws IOException {
		List<String> values = csvParser.nextRow();
		if (values.size() == 0) {
			return null;
		}
		String name = values.get(0);
		HashedPasswordValue hashedPasswordValue = 
				HashedPasswordValue.newInstance(values.get(1));
		User user = User.newInstance(
				name, hashedPasswordValue.toHashedPassword());
		return new UserCsvRow(user, values);
	}
	
	private final User user;
	private final List<String> values;
	
	private UserCsvRow(final User usr, final List<String> vals) {
		this.user = usr;
		this.values = new ArrayList<String>(vals);
	}
	
	@Override
	public String toString() {
		return this.values.stream().collect(Collectors.joining(","));
	}

	public User toUser() {
		return this.user;
	}
	
}
