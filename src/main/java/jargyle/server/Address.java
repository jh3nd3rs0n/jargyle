package jargyle.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Address {

	public static Address newInstance(
			final String s) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(s);
		return new Address(inetAddress, s);
	}
	
	private final InetAddress inetAddress;
	private final String string;
	
	private Address(final InetAddress inetAddr, final String str) {
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
