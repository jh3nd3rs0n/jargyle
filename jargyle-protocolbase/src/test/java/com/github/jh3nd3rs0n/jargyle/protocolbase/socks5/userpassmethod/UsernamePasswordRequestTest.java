package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import org.junit.Assert;
import org.junit.Test;

public class UsernamePasswordRequestTest {

	@Test
	public void testNewInstanceStringCharArray01() {
		UsernamePasswordRequest usernamePasswordRequest1 = 
				UsernamePasswordRequest.newInstance(
						"Aladdin", 
						new char[] { 'o', 'p', 'e', 'n', 's', 'e', 's', 'a', 'm', 'e' });
		UsernamePasswordRequest usernamePasswordRequest2 = 
				UsernamePasswordRequest.newInstance(
						usernamePasswordRequest1.toByteArray());
		Assert.assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
	}

	@Test
	public void testNewInstanceStringCharArray02() {
		UsernamePasswordRequest usernamePasswordRequest1 = 
				UsernamePasswordRequest.newInstance(
						"Jasmine", 
						new char[] { '1', '2', '3', '4', '5', '6' });
		UsernamePasswordRequest usernamePasswordRequest2 = 
				UsernamePasswordRequest.newInstance(
						usernamePasswordRequest1.toByteArray());
		Assert.assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
	}

	@Test
	public void testNewInstanceStringCharArray03() {
		UsernamePasswordRequest usernamePasswordRequest1 = 
				UsernamePasswordRequest.newInstance(
						"Monkey", 
						new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' });
		UsernamePasswordRequest usernamePasswordRequest2 = 
				UsernamePasswordRequest.newInstance(
						usernamePasswordRequest1.toByteArray());
		Assert.assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
	}

}
