package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class ClientMethodSelectionMessageTest {

	@Test
	public void testNewInstanceByteArray01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(new byte[] { 
						(byte) 0x05,
						(byte) 0x01,
						(byte) 0x00 });
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(Methods.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED));
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceByteArray02() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(new byte[] { 
						(byte) 0x05,
						(byte) 0x02,
						(byte) 0x01,
						(byte) 0x00 });
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(Methods.newInstance(
						Method.GSSAPI,
						Method.NO_AUTHENTICATION_REQUIRED));
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceByteArray03() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(new byte[] { 
						(byte) 0x05,
						(byte) 0x03,
						(byte) 0x01,
						(byte) 0x00,
						(byte) 0x02, });
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(Methods.newInstance(
						Method.GSSAPI,
						Method.NO_AUTHENTICATION_REQUIRED,
						Method.USERNAME_PASSWORD));
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceByteArray04() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(new byte[] { 
						(byte) 0x05,
						(byte) 0x06,
						(byte) 0x01,
						(byte) 0x00,
						(byte) 0x02,
						(byte) 0x01,
						(byte) 0x00,
						(byte) 0x02 });
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(Methods.newInstance(
						Method.GSSAPI,
						Method.NO_AUTHENTICATION_REQUIRED,
						Method.USERNAME_PASSWORD,
						Method.GSSAPI,
						Method.NO_AUTHENTICATION_REQUIRED,
						Method.USERNAME_PASSWORD));
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceByteArray05() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(new byte[] { 
						(byte) 0x05,
						(byte) 0x06,
						(byte) 0x01,
						(byte) 0xaa,
						(byte) 0x02,
						(byte) 0xbb,
						(byte) 0x00,
						(byte) 0xcc });
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(Methods.newInstance(
						Method.GSSAPI,
						Method.USERNAME_PASSWORD,
						Method.NO_AUTHENTICATION_REQUIRED));
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceMethods01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceMethods02() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceMethods03() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods04() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(Methods.newInstance());
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods05() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods06() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.GSSAPI,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods07() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods08() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 = 
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		Assert.assertEquals(cmsm1, cmsm2);
	}

}
