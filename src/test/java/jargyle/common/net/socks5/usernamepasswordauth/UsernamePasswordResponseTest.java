package jargyle.common.net.socks5.usernamepasswordauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UsernamePasswordResponseTest {

	@Test
	public void testNewInstanceVersionByte01() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance(
						UsernamePasswordResponse.STATUS_SUCCESS);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}
	
	@Test
	public void testNewInstanceVersionByte02() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance(
						(byte) 0x01);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}

	@Test
	public void testNewInstanceVersionByte03() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance(
						(byte) 0xff);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}


}
