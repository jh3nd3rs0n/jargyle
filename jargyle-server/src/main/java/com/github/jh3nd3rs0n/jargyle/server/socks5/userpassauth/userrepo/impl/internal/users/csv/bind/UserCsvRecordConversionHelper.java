package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

final class UserCsvRecordConversionHelper {
	
	public static User newUserFrom(
			final CsvFileReader csvFileReader) throws IOException {
		List<String> fields = csvFileReader.readRecord();
		if (fields.size() == 0) {
			return null;
		}
		String name = fields.get(0);
		HashedPassword hashedPassword = HashedPassword.newInstance(
				fields.get(1));
		return User.newInstance(name, hashedPassword);
	}
	
	public static void toCsvRecord(
			final User user, final CsvFileWriter csvFileWriter) 
			throws IOException {
		csvFileWriter.writeRecord(
				user.getName(), user.getHashedPassword().toString());
	}
	
	private UserCsvRecordConversionHelper() { }
	
}
