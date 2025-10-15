package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;

import java.io.IOException;
import java.net.Socket;

final class NoAcceptableMethodsMethodSubNegotiator
        extends MethodSubNegotiator {

    public NoAcceptableMethodsMethodSubNegotiator() {
        super(Method.NO_ACCEPTABLE_METHODS);
    }

    @Override
    public MethodEncapsulation subNegotiate(
            final Socket Socket,
            final Socks5Client socks5Client) throws IOException {
        throw new MethodSubNegotiationException(
                this.getMethod(), "no acceptable methods");
    }

}
