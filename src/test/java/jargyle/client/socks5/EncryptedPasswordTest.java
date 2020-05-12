package jargyle.client.socks5;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import jargyle.client.socks5.EncryptedPassword;

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
		char[] password = "Ali-Baba".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

	@Test
	public void testGetPassword03() {
		char[] password = "ooh-ooh-ahh-ahh".toCharArray();
		EncryptedPassword encryptedPassword = EncryptedPassword.newInstance(
				password);
		assertArrayEquals(password, encryptedPassword.getPassword());
	}

}
