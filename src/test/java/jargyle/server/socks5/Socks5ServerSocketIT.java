package jargyle.server.socks5;
import static jargyle.server.ServerSocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.ServerSocketIT.echoThroughServerSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.Configurations;

public class Socks5ServerSocketIT {
	
	@Test
	public void testThroughSocks5ServerSocket01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "Hello, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				Configurations.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocket02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				Configurations.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocket03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = "Goodbye, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				Configurations.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username/password authentication...");
		String string = "Hello, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				Configurations.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username/password authentication...");
		String string = "The quick brown fox jumped over the lazy dog";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "Ali-Baba".toCharArray())),
				Configurations.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username/password authentication...");
		String string = "Goodbye, World";
		String returningString = echoThroughServerSocket(
				string, 
				Socks5Clients.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Apu", "ooh-ooh-ahh-ahh".toCharArray())),
				Configurations.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
}