package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

final class UserCsvRecordConversionHelper {
	
	public static User newUserFrom(
			final CsvFileReader csvFileReader) throws IOException {
		List<String> fields = csvFileReader.nextRecord();
		if (fields.size() == 0) {
			return null;
		}
		String name = fields.get(0);
		HashedPassword hashedPassword = HashedPassword.newInstance(
				fields.get(1));
		return User.newInstance(name, hashedPassword);
	}

	private static String toCsvField(final String value) {
		String field = value;
		if (field.indexOf('\"') > -1) {
			field = field.replace("\"", "\"\"");
		}
		if (field.indexOf('\"') > -1 
				|| field.indexOf(',') > -1
				|| field.indexOf('\r') > -1
				|| field.indexOf('\n') > -1) {
			field = String.format("\"%s\"", field);
		}
		return field;
	}
	
	public static String toCsvRecord(final User user) {
		StringBuilder sb = new StringBuilder();
		sb.append(toCsvField(user.getName()));
		sb.append(',');
		sb.append(toCsvField(user.getHashedPassword().toString()));
		return sb.toString();
	}
	
	private UserCsvRecordConversionHelper() { }
	
}
