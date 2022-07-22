package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.server.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.transport.socks.SocksException;

public final class ClientFacingIOExceptionLoggingHelper {

	public static void log(
			final Logger logger, final String message, final IOException e) {
		if (e instanceof EOFException 
				|| ThrowableHelper.getRecentCause(
						e, EOFException.class) != null) {
			logger.debug(message, e);
			return;
		}
		if (e instanceof SocketException
				|| ThrowableHelper.getRecentCause(
						e, SocketException.class) != null) {
			logger.debug(message, e);
			return;
		}
		if (e instanceof SocketTimeoutException 
				|| ThrowableHelper.getRecentCause(
						e, SocketTimeoutException.class) != null) {
			logger.debug(message, e);
			return;
		}
		if (e instanceof SocksException
				|| ThrowableHelper.getRecentCause(
						e, SocksException.class) != null) {
			logger.debug(message, e);
			return;
		}
		logger.error(message, e);
	}
	
	private ClientFacingIOExceptionLoggingHelper() { }
	
}
