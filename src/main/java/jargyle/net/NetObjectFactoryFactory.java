package jargyle.net;

public abstract class NetObjectFactoryFactory {
	
	public abstract DatagramSocketFactory newDatagramSocketFactory();
	
	public abstract HostResolverFactory newHostResolverFactory();
	
	public abstract ServerSocketFactory newServerSocketFactory();
	
	public abstract SocketFactory newSocketFactory();
	
}
