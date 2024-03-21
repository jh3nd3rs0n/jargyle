package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod;

import org.junit.Assert;
import org.junit.Test;

public class UsernamePasswordResponseTest {

	@Test
	public void testToByteArray01() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance(
						UsernamePasswordResponse.STATUS_SUCCESS);
		UsernamePasswordResponse usernamePasswordResponse2 =
				UsernamePasswordResponse.newInstanceFrom(
						usernamePasswordResponse1.toByteArray());
		Assert.assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}
	
	@Test
	public void testToByteArray02() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance((byte) 0x01);
		UsernamePasswordResponse usernamePasswordResponse2 =
				UsernamePasswordResponse.newInstanceFrom(
						usernamePasswordResponse1.toByteArray());
		Assert.assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}

	@Test
	public void testToByteArray03() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance((byte) 0xff);
		UsernamePasswordResponse usernamePasswordResponse2 =
				UsernamePasswordResponse.newInstanceFrom(
						usernamePasswordResponse1.toByteArray());
		Assert.assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}


}
