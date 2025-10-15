package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

final class UdpAssociateRequestHandlerFactory
        extends RequestHandlerFactory {

    public UdpAssociateRequestHandlerFactory() {
        super(Command.UDP_ASSOCIATE);
    }

    @Override
    public RequestHandler newRequestHandler(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req) {
        return new UdpAssociateRequestHandler(
                new RequestHandlerContext(
                        handlerContext,
                        methSubNegotiationResults,
                        req,
                        ServerEventLogger.newInstance(UdpAssociateRequestHandler.class)));
    }

}
