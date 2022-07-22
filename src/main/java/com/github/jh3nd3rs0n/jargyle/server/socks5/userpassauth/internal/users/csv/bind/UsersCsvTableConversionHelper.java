package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

public final class UsersCsvTableConversionHelper {

	public static Users newUsersFrom(final Reader reader) throws IOException {
		CsvParser csvParser = new CsvParser(reader);
		User user = null;
		List<User> users = new ArrayList<User>();
		while ((user = UserCsvRowConversionHelper.newUserFrom(
				csvParser)) != null) {
			users.add(user);
		}
		return Users.newInstance(users);
	}
	
	public static void toCsvTable(
			final Users users, final Writer writer) throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		for (User user : users.toMap().values()) {
			writer.write(UserCsvRowConversionHelper.toCsvRow(user));
			writer.write(lineSeparator);
			writer.flush();
		}
	}
	
	private UsersCsvTableConversionHelper() { }
	
}
