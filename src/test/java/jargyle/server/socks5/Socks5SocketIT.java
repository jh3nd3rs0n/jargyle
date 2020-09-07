package jargyle.server.socks5;
import static jargyle.server.SocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.SocketIT.echoThroughSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationFactory;

public class Socks5SocketIT {
	
	@Test
	public void testThroughSocks5Socket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5Socket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5Socket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
}
