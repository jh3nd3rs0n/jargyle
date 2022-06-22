package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;

public final class UsersCsvTable {

	public static UsersCsvTable newInstance(final Users users) {
		List<UserCsvRow> userCsvRows = new ArrayList<UserCsvRow>();
		for (User user : users.toMap().values()) {
			userCsvRows.add(UserCsvRow.newInstance(user));
		}
		return new UsersCsvTable(users, userCsvRows);
	}
	
	public static UsersCsvTable newInstanceFrom(
			final Reader reader) throws IOException {
		CsvParser csvParser = new CsvParser(reader);
		UserCsvRow userCsvRow = null;
		List<UserCsvRow> userCsvRows = new ArrayList<UserCsvRow>();
		List<User> users = new ArrayList<User>();
		while ((userCsvRow = UserCsvRow.newInstanceFrom(csvParser)) != null) {
			userCsvRows.add(userCsvRow);
			users.add(userCsvRow.toUser());
		}
		return new UsersCsvTable(Users.newInstance(users), userCsvRows);
	}
	
	private final List<UserCsvRow> userCsvRows;
	private final Users users;
	
	private UsersCsvTable(final Users usrs, final List<UserCsvRow> usrCsvRows) {
		this.userCsvRows = new ArrayList<UserCsvRow>(usrCsvRows);
		this.users = Users.newInstance(usrs);
	}
	
	public Users toUsers() {
		return Users.newInstance(this.users);
	}
	
	public void toCsv(final Writer writer) throws IOException {
		String lineSeparator = System.getProperty("line.separator");
		for (UserCsvRow userCsvRow : this.userCsvRows) {
			writer.write(userCsvRow.toString());
			writer.write(lineSeparator);
			writer.flush();
		}
	}
	
}
