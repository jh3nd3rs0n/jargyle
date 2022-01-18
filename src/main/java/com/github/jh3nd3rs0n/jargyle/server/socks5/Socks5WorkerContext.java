package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.IOExceptionHandler;
import com.github.jh3nd3rs0n.jargyle.server.WorkerContext;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

public class Socks5WorkerContext extends WorkerContext {
	
	public Socks5WorkerContext(final WorkerContext context) {
		super(context);
	}

	public final boolean sendSocks5Reply(
			final Object worker, 
			final Socks5Reply socks5Rep, 
			final Logger logger) {
		logger.debug(LoggerHelper.objectMessage(worker, String.format(
				"Sending %s",
				socks5Rep.toString())));		
		try {
			this.writeThenFlush(socks5Rep.toByteArray());
		} catch (IOException e) {
			IOExceptionHandler.INSTANCE.handle(
					e, 
					logger, 
					LoggerHelper.objectMessage(
							worker, "Error in writing SOCKS5 reply"));
			return false;
		}
		return true;
	}

}
