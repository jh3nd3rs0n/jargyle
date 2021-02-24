package jargyle.net;

public final class DefaultHostResolverFactory extends HostResolverFactory {

	public DefaultHostResolverFactory() { }
	
	@Override
	public HostResolver newHostResolver() {
		return new HostResolver();
	}

}
