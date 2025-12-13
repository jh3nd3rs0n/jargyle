package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

abstract class MethodSubNegotiator {

    private static final Map<Method, MethodSubNegotiator> METHOD_SUB_NEGOTIATORS_MAP =
            new HashMap<>();

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    private static boolean initialized = false;

	public static MethodSubNegotiator getInstance(final Method meth) {
        initializeIfNotInitialized();
		MethodSubNegotiator methodSubNegotiator = METHOD_SUB_NEGOTIATORS_MAP.get(
				meth);
		if (methodSubNegotiator != null) {
			return methodSubNegotiator;
		}
		String str = METHOD_SUB_NEGOTIATORS_MAP.keySet().stream()
				.map(Method::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				meth));
	}

    private static void initializeIfNotInitialized() {
        REENTRANT_LOCK.lock();
        try {
            if (initialized) {
                return;
            }
            put(new GssapiAuthMethodSubNegotiator());
            put(new NoAcceptableMethodsMethodSubNegotiator());
            put(new NoAuthenticationRequiredMethodSubNegotiator());
            put(new UsernamePasswordAuthMethodSubNegotiator());
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }

    private static void put(final MethodSubNegotiator methodSubNegotiator) {
        METHOD_SUB_NEGOTIATORS_MAP.put(
                methodSubNegotiator.getMethod(), methodSubNegotiator);
    }

	private final Method method;
	
	public MethodSubNegotiator(final Method meth) {
		this.method = meth;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public abstract MethodEncapsulation subNegotiate(
			final Socket socket,
			final Socks5Client socks5Client) throws IOException;
	
}
