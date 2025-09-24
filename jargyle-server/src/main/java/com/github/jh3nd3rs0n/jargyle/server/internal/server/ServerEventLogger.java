package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.SocksException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public final class ServerEventLogger {

    private final Logger logger;

    private ServerEventLogger(final Logger lggr) {
        this.logger = lggr;
    }

    public static ServerEventLogger newInstance(final Class<?> cls) {
        return new ServerEventLogger(LoggerFactory.getLogger(cls));
    }

    public void debug(final String s) {
        this.logger.debug(s);
    }

    public void debug(final String s, final Throwable t) {
        this.logger.debug(s, t);
    }

    public void error(final String s, final Throwable t) {
        this.logger.error(s, t);
    }

    public void logClientIoException(
            final String message, final IOException e) {
        if (ThrowableHelper.isOrHasInstanceOf(e, EOFException.class)) {
            this.logger.debug(message, e);
            return;
        }
        if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
            this.logger.debug(message, e);
            return;
        }
        if (ThrowableHelper.isOrHasInstanceOf(
                e, SocketTimeoutException.class)) {
            this.logger.debug(message, e);
            return;
        }
        if (ThrowableHelper.isOrHasInstanceOf(e, SocksException.class)) {
            this.logger.debug(message, e);
            return;
        }
        this.logger.warn(message, e);
    }

    public void warn(final String s) {
        this.logger.warn(s);
    }

    public void warn(final String s, final Throwable t) {
        this.logger.warn(s, t);
    }

}
