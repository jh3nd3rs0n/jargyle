package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;

import java.io.IOException;
import java.net.Socket;

final class NoAuthenticationRequiredMethodSubNegotiator
        extends MethodSubNegotiator {

    public NoAuthenticationRequiredMethodSubNegotiator() {
        super(Method.NO_AUTHENTICATION_REQUIRED);
    }

    @Override
    public MethodSubNegotiationResults subNegotiate(
            final Socket socket,
            final Configuration configuration) throws IOException {
        MethodEncapsulation methodEncapsulation =
                MethodEncapsulation.newNullInstance(socket);
        return new MethodSubNegotiationResults(
                this.getMethod(), methodEncapsulation, null);
    }

}
