package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.server.internal.server.LogMessageSource;

import java.io.IOException;

abstract class RequestHandler implements LogMessageSource {

    public abstract void handleRequest() throws IOException;

}
