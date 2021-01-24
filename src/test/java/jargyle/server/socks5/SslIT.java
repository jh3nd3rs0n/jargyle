package jargyle.server.socks5;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.server.ConfigurationHelper;
import jargyle.server.DatagramSocketInterfaceIT;
import jargyle.server.ServerSocketInterfaceIT;
import jargyle.server.SocketInterfaceIT;

public class SslIT {

/*	
	@org.junit.BeforeClass
	public static void setUp() {
		System.setProperty("javax.net.debug", "ssl,handshake");
	}
	
	@org.junit.AfterClass
	public static void tearDown() {
		System.clearProperty("javax.net.debug");
	}
*/
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketInterfaceUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketInterfaceIT.echoThroughDatagramSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketInterfaceUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketInterfaceIT.echoThroughServerSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSsl(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketInterfaceUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketInterfaceIT.echoThroughSocketInterface(
				string,
				Socks5ClientHelper.newSocks5ClientUsingSslAndClientAuth(
						SocketInterfaceIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationHelper.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
}
