package jargyle.server.socks5;
import static jargyle.server.DatagramSocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.DatagramSocketIT.echoThroughDatagramSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationFactory;

public class Socks5DatagramSocketIT {

	@Test
	public void testThroughSocks5DatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

}
