package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class RequestHandlerFactories {

    private final Map<Command, RequestHandlerFactory> requestHandlerFactoryMap;

    public RequestHandlerFactories() {
        this.requestHandlerFactoryMap = new HashMap<>();
    }

    public void add(final RequestHandlerFactory value) {
        this.requestHandlerFactoryMap.put(value.getCommand(), value);
    }

    public Map<Command, RequestHandlerFactory> toMap() {
        return Collections.unmodifiableMap(this.requestHandlerFactoryMap);
    }

}
