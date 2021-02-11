package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;

public final class DefaultHostnameResolver extends HostnameResolver {

	public DefaultHostnameResolver() { }
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		return InetAddress.getByName(host);
	}

}
