package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import org.junit.Assert;
import org.junit.Test;

public class HashedPasswordTest {

	@Test
	public void testNewInstanceString01() {
		char[] password = "opensesame".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		Assert.assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceString02() {
		char[] password = "mission:impossible".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		Assert.assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceString03() {
		char[] password = "safeDriversSave40%".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				hashedPassword1.toString());
		Assert.assertEquals(hashedPassword1, hashedPassword2);
	}

}
