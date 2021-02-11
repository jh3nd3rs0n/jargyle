package jargyle.net;

import java.io.IOException;
import java.net.InetAddress;

public abstract class HostnameResolver {

	public abstract InetAddress resolve(final String host) throws IOException;
	
}
