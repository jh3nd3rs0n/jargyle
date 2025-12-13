package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResponseTest {

	@Test
	public void testEqualsObject01() {
		Response response = Response.newInstance(Response.STATUS_SUCCESS);
		Assert.assertEquals(response, response);
	}

	@Test
	public void testEqualsObject02() {
		Response response = Response.newInstance(Response.STATUS_SUCCESS);
		Assert.assertNotEquals(response, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = Response.newInstance(Response.STATUS_SUCCESS);
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(UnsignedByte.valueOf((byte) 0x01));
		Assert.assertNotEquals(response1, response2);
	}

	@Test
	public void testEqualsObject05() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(UnsignedByte.valueOf((byte) 0xff));
		Assert.assertNotEquals(response1, response2);
	}

	@Test
	public void testEqualsObject06() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(Response.STATUS_SUCCESS);
		Assert.assertEquals(response1, response2);
	}

	@Test
	public void testHashCode01() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(UnsignedByte.valueOf((byte) 0x01));
		Assert.assertNotEquals(response1.hashCode(), response2.hashCode());
	}

	@Test
	public void testHashCode02() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(UnsignedByte.valueOf((byte) 0xff));
		Assert.assertNotEquals(response1.hashCode(), response2.hashCode());
	}

	@Test
	public void testHashCode03() {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstance(Response.STATUS_SUCCESS);
		Assert.assertEquals(response1.hashCode(), response2.hashCode());
	}

	@Test
	public void testToByteArray01() throws IOException {
		Response response1 = Response.newInstance(Response.STATUS_SUCCESS);
		Response response2 = Response.newInstanceFrom(new ByteArrayInputStream(
				response1.toByteArray()));
		Assert.assertEquals(response1, response2);
	}
	
	@Test
	public void testToByteArray02() throws IOException {
		Response response1 = Response.newInstance(UnsignedByte.valueOf((byte) 0x01));
		Response response2 = Response.newInstanceFrom(new ByteArrayInputStream(
				response1.toByteArray()));
		Assert.assertEquals(response1, response2);
	}

	@Test
	public void testToByteArray03() throws IOException {
		Response response1 = Response.newInstance(UnsignedByte.valueOf((byte) 0xff));
		Response response2 = Response.newInstanceFrom(new ByteArrayInputStream(
				response1.toByteArray()));
		Assert.assertEquals(response1, response2);
	}


}
