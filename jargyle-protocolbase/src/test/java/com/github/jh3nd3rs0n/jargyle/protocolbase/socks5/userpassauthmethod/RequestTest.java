package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class RequestTest {

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException01() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] { (byte) 0x00 }));
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException02() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x01, (byte) 0x07 }));
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException03() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x01,
				(byte) 0x07,
				(byte) 'A',
				(byte) 'l',
				(byte) 'a',
				(byte) 'd' }));
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException04() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x01,
				(byte) 0x07,
				(byte) 'A',
				(byte) 'l',
				(byte) 'a',
				(byte) 'd',
				(byte) 'd',
				(byte) 'i',
				(byte) 'n',
				(byte) 0x0a }));
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException05() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x01,
				(byte) 0x07,
				(byte) 'A',
				(byte) 'l',
				(byte) 'a',
				(byte) 'd',
				(byte) 'd',
				(byte) 'i',
				(byte) 'n',
				(byte) 0x0a,
				(byte) 'o',
				(byte) 'p',
				(byte) 'e',
				(byte) 'n' }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidatePasswordCharArrayForIllegalArgumentException01() {
		char[] password = new char[Request.MAX_PASSWD_LENGTH + 1];
		Arrays.fill(password, 'a');
		Request.validatePassword(password);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidateUsernameStringForIllegalArgumentException01() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < Request.MAX_UNAME_LENGTH + 1; i++) {
			stringBuilder.append('a');
		}
		String username = stringBuilder.toString();
		Request.validateUsername(username);
	}

	@Test
	public void testEqualsObject01() {
		Request request = Request.newInstance("", new char[] { });
		Assert.assertEquals(request, request);
	}

	@Test
	public void testEqualsObject02() {
		Request request = Request.newInstance("", new char[] { });
		Assert.assertNotEquals(request, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = Request.newInstance("", new char[] { });
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("a", new char[] { });
		Assert.assertNotEquals(request1, request2);
	}

	@Test
	public void testEqualsObject05() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("", new char[] { 'a' });
		Assert.assertNotEquals(request1, request2);
	}

	@Test
	public void testEqualsObject06() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("", new char[] { });
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testHashCode01() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("a", new char[] { });
		Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testHashCode02() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("", new char[] { 'a' });
		Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testHashCode03() {
		Request request1 = Request.newInstance("", new char[] { });
		Request request2 = Request.newInstance("", new char[] { });
		Assert.assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testToByteArray01() throws IOException {
		Request request1 = Request.newInstance("",	new char[] { });
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testToByteArray02() throws IOException {
		Request request1 = Request.newInstance(
				"Aladdin",
				new char[] { 'o', 'p', 'e', 'n', 's', 'e', 's', 'a', 'm', 'e' });
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testToByteArray03() throws IOException {
		Request request1 = Request.newInstance(
				"Jasmine",
				new char[] { '1', '2', '3', '4', '5', '6' });
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testToByteArray04() throws IOException {
		Request request1 = Request.newInstance(
				"Monkey",
				new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' });
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

}
