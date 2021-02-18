package jargyle.net;

public final class DefaultNetFactory extends NetFactory {

	public DefaultNetFactory() { }
	
	@Override
	public DatagramSocketInterfaceFactory newDatagramSocketInterfaceFactory() {
		return new DirectDatagramSocketInterfaceFactory();
	}

	@Override
	public HostnameResolverFactory newHostnameResolverFactory() {
		return new DefaultHostnameResolverFactory();
	}

	@Override
	public ServerSocketInterfaceFactory newServerSocketInterfaceFactory() {
		return new DirectServerSocketInterfaceFactory();
	}

	@Override
	public SocketInterfaceFactory newSocketInterfaceFactory() {
		return new DirectSocketInterfaceFactory();
	}

}
