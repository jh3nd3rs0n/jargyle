package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.internal.userrepo.impl.users.file.bind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.Users;

public final class UsersFileConversionHelper {

	public static Users newUsersFrom(final Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		User user = null;
		List<User> users = new ArrayList<User>();
		while ((user = UserLineConversionHelper.newUserFrom(
				bufferedReader)) != null) {
			users.add(user);
		}
		return Users.of(users);
	}
	
	public static void toFile(
			final Users users, final Writer writer) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		for (User user : users.toMap().values()) {
			UserLineConversionHelper.toLine(user, bufferedWriter);
		}
	}
	
	private UsersFileConversionHelper() { }
	
}
