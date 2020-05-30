package jargyle.common.net.socks5;

import static org.junit.Assert.assertEquals;

import java.util.EnumSet;

import org.junit.Test;

public class ClientMethodSelectionMessageTest {

	@Test
	public void testNewInstanceVersionSetOfMethod01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						EnumSet.of(
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceVersionSetOfMethod02() {
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
	public void testNewInstanceVersionSetOfMethod03() {
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

}
