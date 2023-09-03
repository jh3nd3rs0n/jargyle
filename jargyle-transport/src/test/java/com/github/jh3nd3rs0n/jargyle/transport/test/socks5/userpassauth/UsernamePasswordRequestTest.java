package com.github.jh3nd3rs0n.jargyle.transport.test.socks5.userpassauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;

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
		assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
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
		assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
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
		assertEquals(usernamePasswordRequest1, usernamePasswordRequest2);
	}

}
