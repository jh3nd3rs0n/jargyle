package com.github.jh3nd3rs0n.jargyle.common.test.security;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

public class EncryptedPasswordTest {

	@Test
	public void testGetPassword01() {
		char[] password = "opensesame".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword02() {
		char[] password = "mission%3Aimpossible".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword03() {
		char[] password = "safeDriversSave40%25".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

}
