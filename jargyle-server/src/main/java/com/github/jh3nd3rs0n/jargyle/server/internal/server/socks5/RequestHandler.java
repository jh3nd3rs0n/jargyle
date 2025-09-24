package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;

abstract class RequestHandler {

    public abstract void handleRequest() throws IOException;

}
