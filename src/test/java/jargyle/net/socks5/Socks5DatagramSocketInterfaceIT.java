package jargyle.net.socks5;
import static jargyle.net.DatagramSocketInterfaceIT.LOOPBACK_ADDRESS;
import static jargyle.net.DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.socks.ConfigurationHelper;
import jargyle.net.socks.SocksClientHelper;

public class Socks5DatagramSocketInterfaceIT {

	@Test
	public void testThroughSocks5DatagramSocketInterface01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterface02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterface03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughDatagramSocketInterface(
				string, 
				SocksClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

}
