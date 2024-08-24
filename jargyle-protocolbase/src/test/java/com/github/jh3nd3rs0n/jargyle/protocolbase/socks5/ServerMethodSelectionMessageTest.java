package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ServerMethodSelectionMessageTest {

	@Test
	public void testEqualsObject01() {
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		Assert.assertEquals(smsm, smsm);
	}

	@Test
	public void testEqualsObject02() {
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		Assert.assertNotEquals(smsm, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		Assert.assertNotEquals(smsm1, smsm2);
	}

	@Test
	public void testEqualsObject05() {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		Assert.assertEquals(smsm1, smsm2);
	}

	@Test
	public void testHashCode01() {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		Assert.assertNotEquals(smsm1.hashCode(), smsm2.hashCode());
	}

	@Test
	public void testHashCode02() {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		Assert.assertEquals(smsm1.hashCode(), smsm2.hashCode());
	}

	@Test
	public void testToByteArray01() throws IOException {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstanceFrom(
						new ByteArrayInputStream(smsm1.toByteArray()));
		Assert.assertEquals(smsm1, smsm2);
	}

	@Test
	public void testToByteArray02() throws IOException {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstanceFrom(
						new ByteArrayInputStream(smsm1.toByteArray()));
		Assert.assertEquals(smsm1, smsm2);
	}

	@Test
	public void testToByteArray03() throws IOException {
		ServerMethodSelectionMessage smsm1 =
				ServerMethodSelectionMessage.newInstance(
						Method.USERNAME_PASSWORD);
		ServerMethodSelectionMessage smsm2 =
				ServerMethodSelectionMessage.newInstanceFrom(
						new ByteArrayInputStream(smsm1.toByteArray()));
		Assert.assertEquals(smsm1, smsm2);
	}

}
