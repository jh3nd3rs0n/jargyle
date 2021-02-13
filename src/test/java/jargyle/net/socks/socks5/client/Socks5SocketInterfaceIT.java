package jargyle.net.socks.socks5.client;
import static jargyle.net.SocketInterfaceIT.LOOPBACK_ADDRESS;
import static jargyle.net.SocketInterfaceIT.echoThroughSocketInterface;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.server.ConfigurationHelper;

public class Socks5SocketInterfaceIT {
	
	@Test
	public void testThroughSocks5SocketInterface01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterface02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterface03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
}
