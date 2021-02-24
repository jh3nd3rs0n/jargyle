package jargyle.net;

public final class DefaultHostnameResolverFactory 
	extends HostResolverFactory {

	public DefaultHostnameResolverFactory() { }
	
	@Override
	public HostResolver newHostResolver() {
		return new HostResolver();
	}

}
