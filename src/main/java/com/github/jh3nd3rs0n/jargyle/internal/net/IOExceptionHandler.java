package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;

public enum IOExceptionHandler {

	INSTANCE;
	
	public void handle(
			final IOException e, 
			final IOException defaultException) throws IOException {
		if (e instanceof SocketException) {
			throw e;
		}
		if (e instanceof SocketTimeoutException) {
			throw e;
		}
		if (e instanceof SSLException) {
			Throwable cause = e.getCause();
			if (cause == null) {
				throw defaultException;
			}
			if (cause instanceof SocketException) {
				throw (SocketException) cause;
			}
			if (cause instanceof SocketTimeoutException) {
				throw (SocketTimeoutException) cause;
			}
		}
		throw defaultException;		
	}

	public void handle(
			final IOException e, 
			final Logger logger, 
			final String logMessage) {
		if (e instanceof SocketException) {
			logger.debug(logMessage, e);
			return;
		}
		if (e instanceof SocketTimeoutException) {
			logger.error(logMessage, e);
			return;
		}
		if (e instanceof SSLException) {
			Throwable cause = e.getCause();
			if (cause == null) {
				logger.error(logMessage, e);
				return;
			}
			if (cause instanceof SocketException) {
				logger.debug(logMessage, cause);
				return;
			}
			if (cause instanceof SocketTimeoutException) {
				logger.error(logMessage, cause);
				return;
			}
		}
		logger.error(logMessage, e);		
	}

}
