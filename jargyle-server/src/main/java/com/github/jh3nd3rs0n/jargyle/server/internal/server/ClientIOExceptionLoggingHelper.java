package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.internal.lang.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.transport.SocksException;

public final class ClientIOExceptionLoggingHelper {

	public static void log(
			final Logger logger, final String message, final IOException e) {
		if (ThrowableHelper.isOrHasInstanceOf(e, EOFException.class)) {
			logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
			logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(
				e, SocketTimeoutException.class)) {
			logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocksException.class)) {
			logger.debug(message, e);
			return;
		}
		logger.error(message, e);
	}
	
	private ClientIOExceptionLoggingHelper() { }
	
}
