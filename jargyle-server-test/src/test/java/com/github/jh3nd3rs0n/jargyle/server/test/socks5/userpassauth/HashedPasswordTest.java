package com.github.jh3nd3rs0n.jargyle.server.test.socks5.userpassauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;

public class HashedPasswordTest {

	@Test
	public void testNewInstanceString01() {
		char[] password = "opensesame".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceString02() {
		char[] password = "mission:impossible".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceString03() {
		char[] password = "safeDriversSave40%".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		assertEquals(hashedPassword1, hashedPassword2);
	}

}
