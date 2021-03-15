package jargyle.net;

public final class DefaultNetObjectFactoryFactory 
	extends NetObjectFactoryFactory {

	public DefaultNetObjectFactoryFactory() { }
	
	@Override
	public DatagramSocketFactory newDatagramSocketFactory() {
		return new DefaultDatagramSocketFactory();
	}

	@Override
	public HostResolverFactory newHostResolverFactory() {
		return new DefaultHostResolverFactory();
	}

	@Override
	public ServerSocketFactory newServerSocketFactory() {
		return new DefaultServerSocketFactory();
	}

	@Override
	public SocketFactory newSocketFactory() {
		return new DefaultSocketFactory();
	}

}
