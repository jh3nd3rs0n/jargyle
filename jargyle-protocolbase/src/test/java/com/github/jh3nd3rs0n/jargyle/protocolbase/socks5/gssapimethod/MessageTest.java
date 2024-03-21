package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import org.junit.Assert;
import org.junit.Test;

public class MessageTest {

	@Test
	public void testToByteArray01() {
		Message message1 = Message.newInstance(
				MessageType.ABORT, 
				new byte[]{});
		Message message2 = Message.newInstanceFrom(message1.toByteArray());
		Assert.assertEquals(message1, message2);
	}

	@Test
	public void testToByteArray02() {
		Message message1 = Message.newInstance(
				MessageType.AUTHENTICATION, 
				TestStringConstants.STRING_01.getBytes());
		Message message2 = Message.newInstanceFrom(message1.toByteArray());
		Assert.assertEquals(message1, message2);
	}

	@Test
	public void testToByteArray03() {
		Message message1 = Message.newInstance(
				MessageType.ENCAPSULATED_USER_DATA, 
				TestStringConstants.STRING_02.getBytes());
		Message message2 = Message.newInstanceFrom(message1.toByteArray());
		Assert.assertEquals(message1, message2);
	}

	@Test
	public void testToByteArray04() {
		Message message1 = Message.newInstance(
				MessageType.PROTECTION_LEVEL_NEGOTIATION, 
				TestStringConstants.STRING_03.getBytes());
		Message message2 = Message.newInstanceFrom(message1.toByteArray());
		Assert.assertEquals(message1, message2);
	}

}
