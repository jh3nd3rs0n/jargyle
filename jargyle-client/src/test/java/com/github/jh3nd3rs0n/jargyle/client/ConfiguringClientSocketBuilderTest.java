package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class ConfiguringClientSocketBuilderTest {

    @Test
    public void testConfigure01() throws IOException {
        NonNegativeInteger soTimeout = NonNegativeInteger.valueOf(1000);
        PositiveInteger receiveBufferSize = PositiveInteger.valueOf(4000);
        PositiveInteger sendBufferSize = PositiveInteger.valueOf(7240);
        SocksServerUri socksServerUri = Scheme.SOCKS5.newSocksServerUri(
                "localhost");
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS.newProperty(SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(soTimeout),
                        StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSetting(receiveBufferSize),
                        StandardSocketSettingSpecConstants.SO_SNDBUF.newSocketSetting(sendBufferSize))));
        ClientSocketBuilder clientSocketBuilder =
                socksServerUri.newSocksClient(properties)
                        .newClientSocketBuilder();
        try (Socket socket = new Socket()) {
            ConfiguringClientSocketBuilder configuringClientSocketBuilder =
                    clientSocketBuilder.proceedToConfigure(socket);
            configuringClientSocketBuilder.configure();
            Assert.assertEquals(soTimeout.intValue(), socket.getSoTimeout());
            Assert.assertEquals(
                    receiveBufferSize.intValue(),
                    socket.getReceiveBufferSize());
            Assert.assertEquals(
                    sendBufferSize.intValue(),
                    socket.getSendBufferSize());
        }
    }

}