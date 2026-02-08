package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ReplyTest {

	@Test
	public void testNewFailureInstanceReplyCode01() {
		Reply reply = Reply.newFailureInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE);
		Assert.assertEquals(
				Address.newInstanceFrom("0.0.0.0"),
				reply.getServerBoundAddress());
		Assert.assertEquals(Port.valueOf(0), reply.getServerBoundPort());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNewFailureInstanceReplyCodeForIllegalArgumentException01() {
		Reply.newFailureInstance(ReplyCode.SUCCEEDED);
	}

	@Test
	public void testNewInstanceFromInputStream01() throws IOException {
		Reply reply1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x05,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00 }));
		Assert.assertEquals(reply1, reply2);
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException01() throws IOException {
		Reply.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x05,
				(byte) 0xff, // wrong value for reply code
				(byte) 0x00,
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00 }));
	}

	@Test(expected = IOException.class)
	public void testNewInstanceFromInputStreamForIOException02() throws IOException {
		Reply.newInstanceFrom(new ByteArrayInputStream(new byte[] {
				(byte) 0x05,
				(byte) 0x01,
				(byte) 0x01, // wrong value for reserved field
				(byte) 0x01,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00,
				(byte) 0x00 }));
	}

	@Test
	public void testNewSuccessInstanceAddressPort01() {
		Reply reply = Reply.newSuccessInstance(
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Assert.assertEquals(ReplyCode.SUCCEEDED, reply.getReplyCode());
	}

	@Test
	public void testEqualsObject01() {
		Reply reply = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Assert.assertEquals(reply, reply);
	}

	@Test
	public void testEqualsObject02() {
		Reply reply = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Assert.assertNotEquals(reply, null);
	}

	@Test
	public void testEqualsObject03() {
		Object obj1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Object obj2 = new Object();
		Assert.assertNotEquals(obj1, obj2);
	}

	@Test
	public void testEqualsObject04() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstance(
				ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Assert.assertNotEquals(reply1, reply2);
	}

	@Test
	public void testEqualsObject05() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("::"),
				Port.valueOf(0));
		Assert.assertNotEquals(reply1, reply2);
	}

	@Test
	public void testEqualsObject06() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Reply reply2 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(80));
		Assert.assertNotEquals(reply1, reply2);
	}

	@Test
	public void testEqualsObject07() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Reply reply2 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testHashCode01() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstance(
				ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Assert.assertNotEquals(reply1.hashCode(), reply2.hashCode());
	}

	@Test
	public void testHashCode02() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("0.0.0.0"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstance(
				ReplyCode.GENERAL_SOCKS_SERVER_FAILURE,
				Address.newInstanceFrom("::"),
				Port.valueOf(0));
		Assert.assertNotEquals(reply1.hashCode(), reply2.hashCode());
	}

	@Test
	public void testHashCode03() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Reply reply2 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(80));
		Assert.assertNotEquals(reply1.hashCode(), reply2.hashCode());
	}

	@Test
	public void testHashCode04() {
		Reply reply1 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Reply reply2 = Reply.newInstance(
				ReplyCode.SUCCEEDED,
				Address.newInstanceFrom("example.com"),
				Port.valueOf(443));
		Assert.assertEquals(reply1.hashCode(), reply2.hashCode());
	}

	@Test
	public void testToByteArray01() throws IOException {
		Reply reply1 = Reply.newSuccessInstance(
				Address.newInstanceFrom("12.216.103.24"),
				Port.valueOf(0));
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray02() throws IOException {
		Reply reply1 = Reply.newSuccessInstance(
				Address.newInstanceFrom("example.com"),
				Port.valueOf(1234));
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray03() throws IOException {
		Reply reply1 = Reply.newSuccessInstance(
				Address.newInstanceFrom("abcd:1234:ef56:abcd:789e:f123:456a:b789"),
				Port.valueOf(0xffff));
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray04() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.ADDRESS_TYPE_NOT_SUPPORTED);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray05() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.COMMAND_NOT_SUPPORTED);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray06() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray07() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.CONNECTION_REFUSED);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray08() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray09() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray10() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.NETWORK_UNREACHABLE);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

	@Test
	public void testToByteArray11() throws IOException {
		Reply reply1 = Reply.newFailureInstance(ReplyCode.TTL_EXPIRED);
		Reply reply2 = Reply.newInstanceFrom(
				new ByteArrayInputStream(reply1.toByteArray()));
		Assert.assertEquals(reply1, reply2);
	}

}
