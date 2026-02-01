package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

final class ResolveRequestHandlerFactory
        extends RequestHandlerFactory {

    public ResolveRequestHandlerFactory() {
        super(Command.RESOLVE);
    }

    @Override
    public RequestHandler newRequestHandler(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req) {
        return new ResolveRequestHandler(
                new RequestHandlerContext(
                        handlerContext,
                        methSubNegotiationResults,
                        req,
                        ServerEventLogger.newInstance(ResolveRequestHandler.class)));
    }

}
