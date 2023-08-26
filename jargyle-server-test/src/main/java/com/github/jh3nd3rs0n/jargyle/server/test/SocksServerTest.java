package com.github.jh3nd3rs0n.jargyle.server.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

public class SocksServerTest {

	@Test
	public void testGetHostForChangingConfiguration() throws IOException {
		Configuration configuration = Configuration.newModifiableInstance();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			configuration.addSetting(GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST.newSetting(
					Host.newInstance("127.0.0.1")));
			Host expectedHost = 
					GeneralSettingSpecConstants.BIND_HOST.getDefaultSetting().getValue();
			Host actualHost = socksServer.getHost();
			assertEquals(expectedHost, actualHost);
		} finally {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
	}

	@Test
	public void testGetPortForChangingConfiguration() throws IOException {
		Configuration configuration = Configuration.newModifiableInstance();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			configuration.addSetting(GeneralSettingSpecConstants.PORT.newSetting(
					Port.newInstance(3000)));
			Port expectedPort = SocksServer.DEFAULT_PORT;
			Port actualPort = socksServer.getPort();
			assertEquals(expectedPort, actualPort);
		} finally {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
	}

}
