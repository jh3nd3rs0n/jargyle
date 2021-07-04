package jargyle.net.socks.transport.v5;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ClientMethodSelectionMessageTest {

	@Test
	public void testNewInstanceListOfMethod01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceListOfMethod02() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceListOfMethod03() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceListOfMethod04() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(Arrays.asList());
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceListOfMethod05() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceListOfMethod06() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.GSSAPI,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceListOfMethod07() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceListOfMethod08() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Arrays.asList(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}

}
