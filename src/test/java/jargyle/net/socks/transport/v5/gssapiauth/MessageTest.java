package jargyle.net.socks.transport.v5.gssapiauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jargyle.TestStringConstants;

public class MessageTest {

	@Test
	public void testNewInstanceVersionMessageTypeByteArray01() {
		Message message1 = Message.newInstance(
				MessageType.ABORT, 
				null);
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceVersionMessageTypeByteArray02() {
		Message message1 = Message.newInstance(
				MessageType.AUTHENTICATION, 
				TestStringConstants.STRING_01.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceVersionMessageTypeByteArray03() {
		Message message1 = Message.newInstance(
				MessageType.ENCAPSULATED_USER_DATA, 
				TestStringConstants.STRING_02.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceVersionMessageTypeByteArray04() {
		Message message1 = Message.newInstance(
				MessageType.PROTECTION_LEVEL_NEGOTIATION, 
				TestStringConstants.STRING_03.getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

}
