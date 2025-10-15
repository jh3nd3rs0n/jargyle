package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;

import java.io.IOException;
import java.net.Socket;

final class NoAcceptableMethodsMethodSubNegotiator
        extends MethodSubNegotiator {

    public NoAcceptableMethodsMethodSubNegotiator() {
        super(Method.NO_ACCEPTABLE_METHODS);
    }

    @Override
    public MethodSubNegotiationResults subNegotiate(
            final Socket socket,
            final Configuration configuration) throws IOException {
        throw new MethodSubNegotiationException(
                this.getMethod(),
                String.format("no acceptable methods from %s", socket));
    }

}
