package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class MethodSubNegotiators {

    private final Map<Method, MethodSubNegotiator> methodSubNegotiatorsMap;

    public MethodSubNegotiators() {
        this.methodSubNegotiatorsMap =
                new HashMap<Method, MethodSubNegotiator>();
    }

    public void add(final MethodSubNegotiator value) {
        this.methodSubNegotiatorsMap.put(value.getMethod(), value);
    }

    public Map<Method, MethodSubNegotiator> toMap() {
        return Collections.unmodifiableMap(this.methodSubNegotiatorsMap);
    }

}
