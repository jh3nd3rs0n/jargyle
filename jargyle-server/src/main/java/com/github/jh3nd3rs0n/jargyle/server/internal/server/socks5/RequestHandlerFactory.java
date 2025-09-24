package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

abstract class RequestHandlerFactory {

    private static final Map<Command, RequestHandlerFactory> REQUEST_HANDLER_FACTORY_MAP;

    static {
        REQUEST_HANDLER_FACTORY_MAP = new HashMap<>();
        RequestHandlerFactory bindRequestHandlerFactory =
                new BindRequestHandlerFactory();
        REQUEST_HANDLER_FACTORY_MAP.put(
                bindRequestHandlerFactory.getCommand(),
                bindRequestHandlerFactory);
        RequestHandlerFactory connectRequestHandlerFactory =
                new ConnectRequestHandlerFactory();
        REQUEST_HANDLER_FACTORY_MAP.put(
                connectRequestHandlerFactory.getCommand(),
                connectRequestHandlerFactory);
        RequestHandlerFactory resolveRequestHandlerFactory =
                new ResolveRequestHandlerFactory();
        REQUEST_HANDLER_FACTORY_MAP.put(
                resolveRequestHandlerFactory.getCommand(),
                resolveRequestHandlerFactory);
        RequestHandlerFactory udpAssociateRequestHandlerFactory =
                new UdpAssociateRequestHandlerFactory();
        REQUEST_HANDLER_FACTORY_MAP.put(
                udpAssociateRequestHandlerFactory.getCommand(),
                udpAssociateRequestHandlerFactory);
    }

    private final Command command;

    private RequestHandlerFactory(final Command cmd) {
        this.command = cmd;
    }

    public static RequestHandlerFactory getInstance(final Command cmd) {
        RequestHandlerFactory requestHandlerFactory =
                REQUEST_HANDLER_FACTORY_MAP.get(cmd);
        if (requestHandlerFactory != null) {
            return requestHandlerFactory;
        }
        String str = REQUEST_HANDLER_FACTORY_MAP.keySet().stream()
                .map(Command::toString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected command must be one of the following values: %s. "
                        + "actual value is %s",
                str,
                cmd));
    }

    public Command getCommand() {
        return this.command;
    }

    public abstract RequestHandler newRequestHandler(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req);

    private static final class BindRequestHandlerFactory
            extends RequestHandlerFactory {

        public BindRequestHandlerFactory() {
            super(Command.BIND);
        }

        @Override
        public RequestHandler newRequestHandler(
                final Socks5ConnectionHandlerContext handlerContext,
                final MethodSubNegotiationResults methSubNegotiationResults,
                final Request req) {
            return new BindRequestHandler(new TcpBasedRequestHandlerContext(
                    new RequestHandlerContext(
                            handlerContext,
                            methSubNegotiationResults,
                            req,
                            ServerEventLogger.newInstance(BindRequestHandler.class))));
        }

    }

    private static final class ConnectRequestHandlerFactory
            extends RequestHandlerFactory {

        public ConnectRequestHandlerFactory() {
            super(Command.CONNECT);
        }

        @Override
        public RequestHandler newRequestHandler(
                final Socks5ConnectionHandlerContext handlerContext,
                final MethodSubNegotiationResults methSubNegotiationResults,
                final Request req) {
            return new ConnectRequestHandler(new TcpBasedRequestHandlerContext(
                    new RequestHandlerContext(
                            handlerContext, 
                            methSubNegotiationResults, 
                            req, 
                            ServerEventLogger.newInstance(ConnectRequestHandler.class))));
        }

    }

    private static final class ResolveRequestHandlerFactory
            extends RequestHandlerFactory {

        public ResolveRequestHandlerFactory() {
            super(Command.RESOLVE);
        }

        @Override
        public RequestHandler newRequestHandler(
                final Socks5ConnectionHandlerContext handlerContext,
                final MethodSubNegotiationResults methSubNegotiationResults,
                final Request req) {
            return new ResolveRequestHandler(new TcpBasedRequestHandlerContext(
                    new RequestHandlerContext(
                            handlerContext,
                            methSubNegotiationResults,
                            req,
                            ServerEventLogger.newInstance(ResolveRequestHandler.class))));
        }

    }

    private static final class UdpAssociateRequestHandlerFactory
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

}
