package jargyle.net.socks.server;

import jargyle.net.socks.server.v5.Socks5Worker;
import jargyle.net.socks.server.v5.Socks5WorkerContext;
import jargyle.net.socks.server.v5.Socks5WorkerFactory;

final class Socks5WorkerFactoryImpl extends Socks5WorkerFactory {

	private static final Socks5WorkerFactoryImpl INSTANCE = 
			new Socks5WorkerFactoryImpl();
	
	public static Socks5WorkerFactoryImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	protected Socks5Worker newSocks5Worker(final Socks5WorkerContext context) {
		return super.newSocks5Worker(context);
	}
	
}
