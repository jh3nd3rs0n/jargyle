package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5ConnectionHandler;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5ConnectionHandlerContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

final class SocksConnectionHandler {

    private final ServerEventLogger serverEventLogger;
    private final SocksConnectionHandlerContext socksConnectionHandlerContext;

    public SocksConnectionHandler(
            final SocksConnectionHandlerContext handlerContext) {
        this.serverEventLogger = handlerContext.getServerEventLogger();
        this.socksConnectionHandlerContext = handlerContext;
    }

    public void handleSocksConnection() throws IOException {
        Socket clientSocket =
                this.socksConnectionHandlerContext.getClientSocket();
        InputStream clientInputStream = clientSocket.getInputStream();
        int version;
        try {
            version = clientInputStream.read();
        } catch (IOException e) {
            this.serverEventLogger.logClientIoException(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "Error in getting the SOCKS version from the "
                                    + "client"),
                    e);
            return;
        }
        if (version == -1) { return; }
        if ((byte) version == com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Version.V5.byteValue()) {
            Socks5ConnectionHandler socks5ConnectionHandler =
                    new Socks5ConnectionHandler(
                            new Socks5ConnectionHandlerContext(
                                    this.socksConnectionHandlerContext,
                                    ServerEventLogger.newInstance(
                                            Socks5ConnectionHandler.class)));
            socks5ConnectionHandler.handleSocks5Connection();
        } else {
            this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "Unknown SOCKS version: %s",
                    version));
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [socksConnectionHandlerContext=" +
                this.socksConnectionHandlerContext +
                "]";
    }

}
