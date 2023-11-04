package com.github.jh3nd3rs0n.jargyle.protocolbase.test.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ServerMethodSelectionMessage;

public class ServerMethodSelectionMessageTest {

	@Test
	public void testNewInstanceMethod01() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceMethod02() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceMethod03() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.USERNAME_PASSWORD);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

}
