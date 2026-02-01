package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

abstract class RequestHandlerFactory {

    private static final Map<Command, RequestHandlerFactory> REQUEST_HANDLER_FACTORY_MAP =
            new HashMap<>();

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    private static boolean initialized = false;

    private final Command command;

    public RequestHandlerFactory(final Command cmd) {
        this.command = cmd;
    }

    public static RequestHandlerFactory getInstance(final Command cmd) {
        initializeIfNotInitialized();
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

    private static void initializeIfNotInitialized() {
        REENTRANT_LOCK.lock();
        try {
            if (initialized) {
                return;
            }
            put(new BindRequestHandlerFactory());
            put(new ConnectRequestHandlerFactory());
            put(new ResolveRequestHandlerFactory());
            put(new UdpAssociateRequestHandlerFactory());
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    private static void put(
            final RequestHandlerFactory requestHandlerFactory) {
        REQUEST_HANDLER_FACTORY_MAP.put(
                requestHandlerFactory.getCommand(), requestHandlerFactory);
    }

    public Command getCommand() {
        return this.command;
    }

    public abstract RequestHandler newRequestHandler(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req);

}
