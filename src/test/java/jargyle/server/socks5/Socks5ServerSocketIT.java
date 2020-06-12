package jargyle.server.socks5;
import static jargyle.server.ServerSocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.ServerSocketIT.echoThroughServerSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationHelper;

public class Socks5ServerSocketIT {
	
	@Test
	public void testThroughSocks5ServerSocket01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "Hello, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocket02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocket03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "Goodbye, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = "Hello, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = "Goodbye, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientHelper.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
}