package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

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
		HashedPassword hashedPassword = HashedPassword.newInstance(
				values.get(1));
		return User.newInstance(name, hashedPassword);
	}

	private static String prepareValue(final String value) {
		String val = value;
		if (val.indexOf('\"') > -1) {
			val = val.replace("\"", "\"\"");
		}
		if (val.indexOf('\"') > -1 
				|| val.indexOf(',') > -1
				|| val.indexOf('\r') > -1
				|| val.indexOf('\n') > -1) {
			val = String.format("\"%s\"", val);
		}
		return val;
	}
	
	public static String toCsvRow(final User user) {
		StringBuilder sb = new StringBuilder();
		sb.append(prepareValue(user.getName()));
		sb.append(',');
		sb.append(prepareValue(user.getHashedPassword().toString()));
		return sb.toString();
	}
	
	private UserCsvRowConversionHelper() { }
	
}
