package jargyle.net.socks.server.v5;

public abstract class Socks5WorkerFactory {

	private static Socks5WorkerFactory instance;
	
	private static void setInstance(final Socks5WorkerFactory factory) {
		if (instance != null) {
			throw new AssertionError(String.format(
					"there can only be one instance of %s", 
					Socks5WorkerFactory.class.getSimpleName()));
		}
		instance = factory;
	}
	
	public Socks5WorkerFactory() {
		setInstance(this);
	}
	
	protected Socks5Worker newSocks5Worker(final Socks5WorkerContext context) {
		return new Socks5Worker(context);
	}
	
}
