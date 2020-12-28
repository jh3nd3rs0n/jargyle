package jargyle.server.socks5;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.server.ConfigurationFactory;
import jargyle.server.DatagramSocketIT;
import jargyle.server.ServerSocketIT;
import jargyle.server.SocketIT;

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
	public void testThroughSocks5DatagramSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketIT.echoThroughDatagramSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						DatagramSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketIT.echoThroughServerSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						ServerSocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSsl(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketIT.echoThroughSocket(
				string,
				Socks5ClientFactory.newSocks5ClientUsingSslAndClientAuth(
						SocketIT.LOOPBACK_ADDRESS.getHostAddress(), 
						null),
				ConfigurationFactory.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
}
