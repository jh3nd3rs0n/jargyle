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
		this.inetAddress = inetAddr;
		this.string = inetAddr.getHostAddress();
	}
	
	public InetAddress toInetAddress() {
		return this.inetAddress;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
