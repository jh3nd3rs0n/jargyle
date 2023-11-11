package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.userrepo.impl.users.file.bind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

final class UserLineConversionHelper {
	
	public static User newUserFrom(
			final BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		return User.newInstanceOfStringContainingHashedPassword(line);
	}
	
	public static void toLine(
			final User user, final BufferedWriter writer) throws IOException {
		writer.write(user.toString());
		writer.newLine();
		writer.flush();
	}
	
	private UserLineConversionHelper() { }
	
}
