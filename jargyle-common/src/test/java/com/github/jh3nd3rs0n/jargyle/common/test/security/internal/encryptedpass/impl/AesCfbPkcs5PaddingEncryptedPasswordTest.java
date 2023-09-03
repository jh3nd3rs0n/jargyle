package com.github.jh3nd3rs0n.jargyle.common.test.security.internal.encryptedpass.impl;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

public class AesCfbPkcs5PaddingEncryptedPasswordTest {

	@Test
	public void testGetPassword01() {
		char[] password = "opensesame".toCharArray();
		AesCfbPkcs5PaddingEncryptedPassword encryptedPassword = 
				AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword02() {
		char[] password = "mission%3Aimpossible".toCharArray();
		AesCfbPkcs5PaddingEncryptedPassword encryptedPassword = 
				AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword03() {
		char[] password = "safeDriversSave40%25".toCharArray();
		AesCfbPkcs5PaddingEncryptedPassword encryptedPassword = 
				AesCfbPkcs5PaddingEncryptedPassword.newInstance(password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

}
