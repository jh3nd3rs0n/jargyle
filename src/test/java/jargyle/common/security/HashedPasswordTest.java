package jargyle.common.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HashedPasswordTest {

	@Test
	public void testNewInstanceCharArrayByteArray01() {
		char[] password = "opensesame".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				password, hashedPassword1);
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray02() {
		char[] password = "mission:impossible".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				password, hashedPassword1);
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray03() {
		char[] password = "safeDriversSave40%".toCharArray();
		HashedPassword hashedPassword1 = HashedPassword.newInstance(password);
		HashedPassword hashedPassword2 = HashedPassword.newInstance(
				password, hashedPassword1);
		assertEquals(hashedPassword1, hashedPassword2);
	}

}
