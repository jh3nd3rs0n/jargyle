package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SocksServerTest {

	@Test
	public void testGetHostForChangingConfiguration() throws IOException {
		Configuration configuration = Configuration.newModifiableInstance();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			configuration.addSetting(GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST.newSetting(
					Host.newInstanceOf("127.0.0.1")));
			Host expectedHost = 
					GeneralSettingSpecConstants.BIND_HOST.getDefaultSetting().getValue();
			Host actualHost = socksServer.getHost();
			Assert.assertEquals(expectedHost, actualHost);
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
					Port.newInstanceOf(3000)));
			Port expectedPort = SocksServer.DEFAULT_PORT;
			Port actualPort = socksServer.getPort();
			Assert.assertEquals(expectedPort, actualPort);
		} finally {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
	}

}
