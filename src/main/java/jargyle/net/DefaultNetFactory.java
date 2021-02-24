package jargyle.net;

public final class DefaultNetFactory extends NetFactory {

	public DefaultNetFactory() { }
	
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
