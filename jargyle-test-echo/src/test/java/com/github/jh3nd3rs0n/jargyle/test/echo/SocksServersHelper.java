package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;
import java.util.List;

public class SocksServersHelper {

    public static void stopSocksServers(
            final List<SocksServer> socksServers) throws IOException {
        for (SocksServer socksServer : socksServers) {
            if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
                socksServer.stop();
            }
        }
    }

    private SocksServersHelper() { }

}
