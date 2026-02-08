package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RequestTest {

	@Test(expected = IOException.class)
	public void testNewRequestFromInputStreamForIOException01() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x05,
				(byte) 0xff, // wrong value for command
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x7f,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00 }));
	}

	@Test(expected = IOException.class)
	public void testNewRequestFromInputStreamForIOException02() throws IOException {
		Request.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x05,
				(byte) 0x01,
				(byte) 0x01, // wrong value for reserved field
				(byte) 0x01,
				(byte) 0x7f,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00 }));
	}

	@Test
	public void testEqualsObject01() {
		Request request = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertEquals(request, request);
	}

	@Test
	public void testEqualsObject02() {
		Request request = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertNotEquals(request, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.BIND,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertNotEquals(request1, request2);
	}

	@Test
	public void testEqualsObject05() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("::1"),
				Port.valueOf(0));
		Assert.assertNotEquals(request1, request2);
	}

	@Test
	public void testEqualsObject06() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(65535));
		Assert.assertNotEquals(request1, request2);
	}

	@Test
	public void testEqualsObject07() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testHashCode01() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.BIND,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testHashCode02() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("::1"),
				Port.valueOf(0));
		Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testHashCode03() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(65535));
		Assert.assertNotEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testHashCode04() {
		Request request1 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Request request2 = Request.newInstance(
				Command.CONNECT,
				Address.newInstanceFrom("127.0.0.1"),
				Port.valueOf(0));
		Assert.assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	public void testToByteArray01() throws IOException {
		Request request1 = Request.newInstance(
				Command.CONNECT, 
				Address.newInstanceFrom("12.216.103.24"),
				Port.valueOf(0));
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testToByteArray02() throws IOException {
		Request request1 = Request.newInstance(
				Command.BIND, 
				Address.newInstanceFrom("example.com"),
				Port.valueOf(1234));
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

	@Test
	public void testToByteArray03() throws IOException {
		Request request1 = Request.newInstance(
				Command.UDP_ASSOCIATE, 
				Address.newInstanceFrom("abcd:1234:ef56:abcd:789e:f123:456a:b789"),
				Port.valueOf(0xffff));
		Request request2 = Request.newInstanceFrom(
				new ByteArrayInputStream(request1.toByteArray()));
		Assert.assertEquals(request1, request2);
	}

}
