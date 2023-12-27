package com.github.jh3nd3rs0n.jargyle.common.security;

import org.junit.Assert;
import org.junit.Test;

public class EncryptedPasswordTest {

	@Test
	public void testGetPassword01() {
		char[] password = "opensesame".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword02() {
		char[] password = "mission%3Aimpossible".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword03() {
		char[] password = "safeDriversSave40%25".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		Assert.assertArrayEquals(password, encryptedPassword.getPassword());
	}

}
