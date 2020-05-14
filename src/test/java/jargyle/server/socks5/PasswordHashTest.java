package jargyle.server.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jargyle.server.socks5.PasswordHash;

public class PasswordHashTest {

	@Test
	public void testNewInstanceCharArrayByteArray01() {
		char[] password = "opensesame".toCharArray();
		PasswordHash passwordHash1 = PasswordHash.newInstance(password);
		PasswordHash passwordHash2 = PasswordHash.newInstance(
				password, passwordHash1.getSalt());
		assertEquals(passwordHash1, passwordHash2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray02() {
		char[] password = "mission:impossible".toCharArray();
		PasswordHash passwordHash1 = PasswordHash.newInstance(password);
		PasswordHash passwordHash2 = PasswordHash.newInstance(
				password, passwordHash1.getSalt());
		assertEquals(passwordHash1, passwordHash2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray03() {
		char[] password = "safeDriversSave40%".toCharArray();
		PasswordHash passwordHash1 = PasswordHash.newInstance(password);
		PasswordHash passwordHash2 = PasswordHash.newInstance(
				password, passwordHash1.getSalt());
		assertEquals(passwordHash1, passwordHash2);
	}

}
