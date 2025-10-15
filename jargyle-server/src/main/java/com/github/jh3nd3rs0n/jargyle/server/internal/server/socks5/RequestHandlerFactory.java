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
            RequestHandlerFactories requestHandlerFactories =
                    new RequestHandlerFactories();
            requestHandlerFactories.add(new BindRequestHandlerFactory());
            requestHandlerFactories.add(new ConnectRequestHandlerFactory());
            requestHandlerFactories.add(new ResolveRequestHandlerFactory());
            requestHandlerFactories.add(new UdpAssociateRequestHandlerFactory());
            REQUEST_HANDLER_FACTORY_MAP.putAll(requestHandlerFactories.toMap());
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    public Command getCommand() {
        return this.command;
    }

    public abstract RequestHandler newRequestHandler(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req);

}
