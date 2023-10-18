package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

public final class UsersCsvFileConversionHelper {

	public static Users newUsersFrom(final Reader reader) throws IOException {
		CsvFileReader csvFileReader = new CsvFileReader(reader);
		User user = null;
		List<User> users = new ArrayList<User>();
		while ((user = UserCsvRecordConversionHelper.newUserFrom(
				csvFileReader)) != null) {
			users.add(user);
		}
		return Users.newInstance(users);
	}
	
	public static void toCsvFile(
			final Users users, final Writer writer) throws IOException {
		CsvFileWriter csvFileWriter = new CsvFileWriter(writer);
		for (User user : users.toMap().values()) {
			UserCsvRecordConversionHelper.toCsvRecord(user, csvFileWriter);
		}
	}
	
	private UsersCsvFileConversionHelper() { }
	
}
