package jargyle.server.socks5;
import static jargyle.server.SocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.SocketIT.echoThroughSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationFactory;

public class Socks5SocketIT {
	
	@Test
	public void testThroughSocks5Socket01() throws IOException {
		System.out.println("Testing through Socks5Socket...");
		String string = "Hello, World";
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5Socket02() throws IOException {
		System.out.println("Testing through Socks5Socket...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5Socket03() throws IOException {
		System.out.println("Testing through Socks5Socket...");
		String string = "Goodbye, World";
		String returningString = echoThroughSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth01() throws IOException {
		System.out.println("Testing through Socks5Socket using username password authentication...");
		String string = "Hello, World";
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
		System.out.println("Testing through Socks5Socket using username password authentication...");
		String string = "The quick brown fox jumped over the lazy dog";
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
		System.out.println("Testing through Socks5Socket using username password authentication...");
		String string = "Goodbye, World";
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
