package jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressProvider {

	private static InetAddressProvider instance;
	
	public static InetAddressProvider getInstance() {
		if (instance == null) {
			instance = new InetAddressProvider();
		}
		return instance;
	}
	
	public static void setInstance(final InetAddressProvider service) {
		instance = service;
	}
	
	protected InetAddressProvider() { }
	
	public InetAddress getInetAddress(
			final String host) throws UnknownHostException {
		return InetAddress.getByName(host);
	}
	
}
