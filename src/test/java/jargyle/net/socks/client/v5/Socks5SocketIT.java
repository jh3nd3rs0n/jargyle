package jargyle.net.socks.client.v5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.NetConstants;
import jargyle.TestStringConstants;
import jargyle.net.SocketHelper;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.ConfigurationHelper;

public class Socks5SocketIT {
	
	@Test
	public void testThroughSocks5Socket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5Socket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5Socket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingUsernamePasswordAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						NetConstants.LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}
}
