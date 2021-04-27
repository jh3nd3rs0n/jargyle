package jargyle.net.socks.transport.v5;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.Test;

public class ClientMethodSelectionMessageTest {

	@Test
	public void testNewInstanceSetOfMethod01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						EnumSet.of(
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceSetOfMethod02() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						EnumSet.of(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceSetOfMethod03() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						EnumSet.of(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceSetOfMethod04() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						EnumSet.noneOf(Method.class));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}

}
