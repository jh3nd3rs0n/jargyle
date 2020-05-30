package jargyle.common.net.socks5.gssapiauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
				"Hello, World".getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceVersionMessageTypeByteArray03() {
		Message message1 = Message.newInstance(
				MessageType.ENCAPSULATED_USER_DATA, 
				"The quick brown fox jumped over the lazy dog.".getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

	@Test
	public void testNewInstanceVersionMessageTypeByteArray04() {
		Message message1 = Message.newInstance(
				MessageType.PROTECTION_LEVEL_NEGOTIATION, 
				"Goodbye, Cruel World".getBytes());
		Message message2 = Message.newInstance(message1.toByteArray());
		assertEquals(message1, message2);
	}

}
