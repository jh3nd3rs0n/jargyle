package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.test.help.TestStringConstants;

public class MessageTest {

	@Test
	public void testNewInstanceMessageTypeByteArray01() {
		Message message1 = Message.newInstance(
				MessageType.ABORT, 
				null);
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceMessageTypeByteArray02() {
		Message message1 = Message.newInstance(
				MessageType.AUTHENTICATION, 
				TestStringConstants.STRING_01.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceMessageTypeByteArray03() {
		Message message1 = Message.newInstance(
				MessageType.ENCAPSULATED_USER_DATA, 
				TestStringConstants.STRING_02.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceMessageTypeByteArray04() {
		Message message1 = Message.newInstance(
				MessageType.PROTECTION_LEVEL_NEGOTIATION, 
				TestStringConstants.STRING_03.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

}
