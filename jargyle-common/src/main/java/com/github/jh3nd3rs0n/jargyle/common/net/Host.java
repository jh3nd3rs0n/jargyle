package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

@SingleValueTypeDoc(
		description = "",
		name = "Host",
		syntax = "HOST_NAME|HOST_ADDRESS",
		syntaxName = "HOST"
)
public abstract class Host {

	static interface InetAddressFactory {

		InetAddress getInetAddress() throws UnknownHostException;

	}

	public static Host newInstance(final String s) {
		try {
			return HostAddress.newHostAddress(s);
		} catch (IllegalArgumentException ignored) {
		}
		try {
			return HostName.newHostName(s);
		} catch (IllegalArgumentException ignored) {
		}
		throw new IllegalArgumentException(String.format(
				"invalid host name or address: %s",
				s));
    }

	private final InetAddressFactory inetAddressFactory;
	private final String string;

	Host(final String str, final InetAddressFactory inetAddrFactory) {
		this.inetAddressFactory = inetAddrFactory;
		this.string = str;
	}

	@Override
	public final boolean equals(Object obj) {
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
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}

	public final InetAddress toInetAddress() throws UnknownHostException {
		return this.inetAddressFactory.getInetAddress();
	}

	@Override
	public final String toString() {
		return this.string;
	}

}
