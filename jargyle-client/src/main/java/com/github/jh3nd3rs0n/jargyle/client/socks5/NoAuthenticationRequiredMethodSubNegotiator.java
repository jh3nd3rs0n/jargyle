package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;

import java.io.IOException;
import java.net.Socket;

final class NoAuthenticationRequiredMethodSubNegotiator
        extends MethodSubNegotiator {

    public NoAuthenticationRequiredMethodSubNegotiator() {
        super(Method.NO_AUTHENTICATION_REQUIRED);
    }

    @Override
    public MethodEncapsulation subNegotiate(
            final Socket socket,
            final Socks5Client socks5Client) throws IOException {
        return MethodEncapsulation.newNullInstance(socket);
    }

}
