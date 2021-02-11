package jargyle.net;

public final class DefaultHostnameResolverFactory 
	extends HostnameResolverFactory {

	public DefaultHostnameResolverFactory() { }
	
	@Override
	public HostnameResolver newHostnameResolver() {
		return new DefaultHostnameResolver();
	}

}
