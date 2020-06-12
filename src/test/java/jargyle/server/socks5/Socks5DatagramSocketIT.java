package jargyle.server.socks5;
import static jargyle.server.DatagramSocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.DatagramSocketIT.echoThroughDatagramSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationHelper;

public class Socks5DatagramSocketIT {

	@Test
	public void testThroughSocks5DatagramSocket01() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket...");
		String string = "Hello, World";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocket02() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocket03() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket...");
		String string = "Goodbye, World";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth01() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket using username password authentication...");
		String string = "Hello, World";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth02() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket using username password authentication...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUsernamePasswordAuth03() throws IOException {
		System.out.println("Testing through Socks5DatagramSocket using username password authentication...");
		String string = "Goodbye, World";
		String returningString = echoThroughDatagramSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

}
