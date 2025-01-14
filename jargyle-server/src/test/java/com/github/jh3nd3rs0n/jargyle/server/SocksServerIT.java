package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SocksServerIT {

	@Test
	public void testGetHostForChangingConfiguration() throws IOException {
		Configuration configuration = Configuration.newModifiableInstance();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			configuration.addSetting(GeneralSettingSpecConstants.SOCKS_SERVER_BIND_HOST.newSetting(
					Host.newInstance("127.0.0.1")));
			Host expectedHost = Host.newInstance("0.0.0.0");
			Host actualHost = socksServer.getHost();
			Assert.assertEquals(expectedHost, actualHost);
		} finally {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testGetPortForChangingConfiguration() throws IOException {
		Configuration configuration = Configuration.newModifiableInstance();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
			configuration.addSetting(GeneralSettingSpecConstants.PORT.newSetting(
					Port.valueOf(3000)));
			Port expectedPort = SocksServer.DEFAULT_PORT;
			Port actualPort = socksServer.getPort();
			Assert.assertEquals(expectedPort, actualPort);
		} finally {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

}
