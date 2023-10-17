package com.github.jh3nd3rs0n.jargyle.server.test.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.Users;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind.UsersCsvFileConversionHelper;

public class UsersCsvFileConversionHelperTest {

	@Test
	public void test() throws IOException {
		String string = new StringBuilder()
				.append("Aladdin,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\r\n")
				.append("Jasmine,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=\r\n")
				.append("Abu,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=\r\n")
				.append("Jafar,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=\r\n")
				.toString();
		Users expectedUsers = Users.newInstance(
				User.newInstance("Aladdin", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")),
				User.newInstance("Jasmine", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=")),
				User.newInstance("Abu", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=")),
				User.newInstance("Jafar", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);
	}
	
	@Test
	public void testWithDifferentLineEndings() throws IOException {
		String string = new StringBuilder()
				.append("Aladdin,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\r\n")
				.append("Jasmine,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=\n")
				.append("Abu,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=\r\n")
				.append("Jafar,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=\n")
				.toString();
		Users expectedUsers = Users.newInstance(
				User.newInstance("Aladdin", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")),
				User.newInstance("Jasmine", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=")),
				User.newInstance("Abu", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=")),
				User.newInstance("Jafar", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);		
	}
	
	@Test(expected = IOException.class)
	public void testWithFieldContainingNonescapedCarriageReturnCharacter() throws IOException {
		String string = "Jafar,com.\rgithub.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=";
		StringReader reader = new StringReader(string);
		UsersCsvFileConversionHelper.newUsersFrom(reader);		
	}
	
	@Test(expected = IOException.class)
	public void testWithFieldContainingNonescapedDoubleQuoteCharacter() throws IOException {
		String string = "Jafar\",com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=";
		StringReader reader = new StringReader(string);
		UsersCsvFileConversionHelper.newUsersFrom(reader);		
	}
	
	@Test
	public void testWithFieldEnclosedWithDoubleQuotesContainingEscapedDoubleQuote() throws IOException {
		String string = "\"Alad\"\"din\",\"com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\"";
		Users expectedUsers = Users.newInstance(
				User.newInstance("Alad\"din", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);
	}
	
	@Test
	public void testWithFieldsEnclosedWithDoubleQuotes() throws IOException {
		String string = new StringBuilder()
				.append("\"Aladdin\",\"com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\"\r\n")
				.append("Jasmine,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=\r\n")
				.append("\"Abu\",\"com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=\"\r\n")
				.append("Jafar,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=\r\n")
				.toString();
		Users expectedUsers = Users.newInstance(
				User.newInstance("Aladdin", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")),
				User.newInstance("Jasmine", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=")),
				User.newInstance("Abu", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=")),
				User.newInstance("Jafar", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);
		
	}
	
	@Test
	public void testWithFieldsEnclosedWithDoubleQuotesContainingLineBreak() throws IOException {
		String string = new StringBuilder()
				.append("\"\nAladdin\",com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\r\n")
				.append("\"Jasmine\r\",com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=\r\n")
				.append("\"Ab\r\nu\",com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=\r\n")
				.append("\"J\r\nafar\",com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=\r\n")
				.toString();
		Users expectedUsers = Users.newInstance(
				User.newInstance("\nAladdin", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")),
				User.newInstance("Jasmine\r", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=")),
				User.newInstance("Ab\r\nu", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=")),
				User.newInstance("J\r\nafar", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);
		
	}
	
	@Test(expected = IOException.class)
	public void testWithIncompleteEscapedField() throws IOException {
		String string = "Jafar,\"com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=";
		StringReader reader = new StringReader(string);
		UsersCsvFileConversionHelper.newUsersFrom(reader);
	}
	
	@Test
	public void testWithNoLineEndingInLastRow() throws IOException {
		String string = new StringBuilder()
				.append("Aladdin,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=\r\n")
				.append("Jasmine,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=\r\n")
				.append("Abu,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=\r\n")
				.append("Jafar,com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")
				.toString();
		Users expectedUsers = Users.newInstance(
				User.newInstance("Aladdin", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=;mGvQZmPl/q4=")),
				User.newInstance("Jasmine", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=;K+aacLMX4TQ=")),
				User.newInstance("Abu", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=;SaTI6PwS6WE=")),
				User.newInstance("Jafar", HashedPassword.newInstance("com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword:Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=;jIBPXJxqlMk=")));
		StringReader reader = new StringReader(string);
		Users actualUsers = UsersCsvFileConversionHelper.newUsersFrom(reader);
		assertEquals(expectedUsers, actualUsers);		
	}

}
