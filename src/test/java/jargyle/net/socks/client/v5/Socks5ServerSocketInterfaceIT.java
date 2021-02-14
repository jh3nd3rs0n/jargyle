package jargyle.net.socks.client.v5;
import static jargyle.net.ServerSocketInterfaceIT.LOOPBACK_ADDRESS;
import static jargyle.net.ServerSocketInterfaceIT.echoThroughServerSocketInterface;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.net.socks.server.ConfigurationHelper;

public class Socks5ServerSocketInterfaceIT {
	
	@Test
	public void testThroughSocks5ServerSocketInterface01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterface02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterface03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughServerSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
}