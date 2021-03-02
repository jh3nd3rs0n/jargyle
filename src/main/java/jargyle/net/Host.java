package jargyle.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Host {
	
	private static Host ipv4WildcardInstance;
	
	private static Host ipv6WildcardInstance;
	
	public static Host getIpv4WildcardInstance() {
		if (ipv4WildcardInstance == null) {
			try {
				ipv4WildcardInstance = Host.newInstance("0.0.0.0");
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}	
		}
		return ipv4WildcardInstance;
	}
	
	public static Host getIpv6WildcardInstance() {
		if (ipv6WildcardInstance == null) {
			try {
				ipv6WildcardInstance = Host.newInstance("0:0:0:0:0:0:0:0");
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
		}
		return ipv6WildcardInstance; 
	}
	
	public static Host newInstance(final String s) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(s);
		return new Host(inetAddress, s);
	}

	private final InetAddress inetAddress;

	private final String string;
	
	private Host(final InetAddress inetAddr, final String str) {
		this.inetAddress = inetAddr;
		this.string = str;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Host other = (Host) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}
	
	public InetAddress toInetAddress() {
		return this.inetAddress;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
