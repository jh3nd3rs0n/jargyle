package jargyle.net.socks.client.v5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.ServerSocketHelper;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.ConfigurationHelper;

public class Socks5ServerSocketIT {
	
	@Test
	public void testThroughSocks5ServerSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}
	
}