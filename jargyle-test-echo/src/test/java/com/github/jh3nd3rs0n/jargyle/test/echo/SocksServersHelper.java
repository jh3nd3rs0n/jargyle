package com.github.jh3nd3rs0n.jargyle.test.echo;

import java.io.IOException;
import java.util.List;

public class SocksServersHelper {

    public static void stopSocksServers(
            final List<AbstractSocksServer> socksServers) throws IOException {
        for (AbstractSocksServer socksServer : socksServers) {
            if (!socksServer.getState().equals(
                    AbstractSocksServer.State.STOPPED)) {
                socksServer.stop();
            }
        }
    }

    private SocksServersHelper() { }

}
