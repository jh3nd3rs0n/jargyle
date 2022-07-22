package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.io.IOException;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

final class UserCsvRowConversionHelper {

	public static User newUserFrom(
			final CsvParser csvParser) throws IOException {
		List<String> values = csvParser.nextRow();
		if (values.size() == 0) {
			return null;
		}
		String name = values.get(0);
		HashedPassword hashedPassword = 
				HashedPasswordValueConversionHelper.toHashedPassword(
						values.get(1));
		return User.newInstance(name, hashedPassword);
	}
	
	public static String toCsvRow(final User user) {
		StringBuilder sb = new StringBuilder();
		String name = user.getName();
		if (name.indexOf(',') > -1 || name.indexOf('\n') > -1) {
			throw new IllegalArgumentException(
					"name must not contain any commas (,) "
					+ "or any newline characters (\\n)");
		}
		sb.append(name);
		sb.append(',');
		sb.append(HashedPasswordValueConversionHelper.toValue(
				user.getHashedPassword()));
		return sb.toString();
	}
	
	private UserCsvRowConversionHelper() { }
	
}
