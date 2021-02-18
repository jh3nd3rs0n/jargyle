package jargyle.net;

public abstract class NetFactory {
	
	public abstract DatagramSocketInterfaceFactory newDatagramSocketInterfaceFactory();
	
	public abstract HostnameResolverFactory newHostnameResolverFactory();
	
	public abstract ServerSocketInterfaceFactory newServerSocketInterfaceFactory();
	
	public abstract SocketInterfaceFactory newSocketInterfaceFactory();
	
}
