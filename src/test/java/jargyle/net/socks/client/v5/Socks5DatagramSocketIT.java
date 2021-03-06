package jargyle.net.socks.client.v5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.net.DatagramSocketHelper;
import jargyle.net.socks.client.SocksClientHelper;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.ConfigurationHelper;

public class Socks5DatagramSocketIT {

	@Test
	public void testThroughSocks5DatagramSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), null), 
				ConfigurationHelper.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUserpassAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingUserpassAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingUserpassAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string, 
				SocksClientHelper.newSocks5Client(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationHelper.newConfigurationUsingSocks5UserpassAuth());
		assertEquals(string, returningString);
	}

}
