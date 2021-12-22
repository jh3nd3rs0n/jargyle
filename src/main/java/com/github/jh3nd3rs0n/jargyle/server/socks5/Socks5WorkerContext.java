package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.WorkerContext;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

public class Socks5WorkerContext extends WorkerContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Socks5WorkerContext.class);
	
	public Socks5WorkerContext(final WorkerContext context) {
		super(context);
	}

	public final boolean sendSocks5Reply(
			final Object worker, final Socks5Reply socks5Rep) {
		LOGGER.debug(LoggerHelper.objectMessage(worker, String.format(
				"Sending %s",
				socks5Rep.toString())));		
		try {
			this.writeThenFlush(socks5Rep.toByteArray());
		} catch (SocketException e) {
			// socket closed
			return false;
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							worker, "Error in writing SOCKS5 reply"), 
					e);
			return false;
		}
		return true;
	}

}
