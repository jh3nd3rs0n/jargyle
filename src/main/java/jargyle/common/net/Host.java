package jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Host {

	public static Host newInstance(final String s) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(s);
		return new Host(inetAddress, s);
	}
	
	private final InetAddress inetAddress;
	private final String string;
	
	private Host(final InetAddress inetAddr, final String str) {
		String hostName = inetAddr.getHostName();
		String hostAddress = inetAddr.getHostAddress();
		String s = (str.equals(hostName)) ? hostName : hostAddress;
		this.inetAddress = inetAddr;
		this.string = s;
	}
	
	public InetAddress toInetAddress() {
		return this.inetAddress;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
