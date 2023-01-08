package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SocksServerHelper {

	public static List<SocksServer> newStartedSocksServers(
			final List<Configuration> configurations) throws IOException {
		int configurationsSize = configurations.size();
		List<SocksServer> socksServers = new ArrayList<SocksServer>();
		if (configurationsSize > 0) {
			for (int i = configurationsSize - 1; i > -1; i--) {
				Configuration configuration = configurations.get(i);
				SocksServer socksServer = new SocksServer(configuration);
				socksServers.add(0, socksServer);
				socksServer.start();
			}
		}
		return Collections.unmodifiableList(socksServers);
	}
	
	public static void stopSocksServers(
			final List<SocksServer> socksServers) throws IOException {
		for (SocksServer socksServer : socksServers) {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
	}
	
	private SocksServerHelper() { }
	
}
