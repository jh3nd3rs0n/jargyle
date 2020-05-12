package jargyle.common.net.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.ServerMethodSelectionMessage;

public class ServerMethodSelectionMessageTest {

	@Test
	public void testNewInstanceVersionMethod01() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceVersionMethod02() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceVersionMethod03() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.USERNAME_PASSWORD);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

}
