package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;

public class HostnameResolver {
	
	public HostnameResolver() { }
	
	public InetAddress resolve(final String host) throws IOException {
		return InetAddress.getByName(host);
	}
	
}
