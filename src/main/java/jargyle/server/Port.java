package jargyle.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;

public final class Port implements Comparable<Port> {

	public static final int MAX_INT_VALUE = 0xffff;
	public static final int MIN_INT_VALUE = 1;
	
	public static Port newInstance(final int i) {
		if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					i));
		}
		return new Port(i);
	}
	
	public static Port newInstance(final String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					s),
					e);
		}
		return newInstance(i);
	}
	
	private final int intValue;
	
	private Port(final int i) {
		this.intValue = i;
	}
	
	@Override
	public int compareTo(final Port other) {
		return this.intValue - other.intValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Port)) {
			return false;
		}
		Port other = (Port) obj;
		if (this.intValue != other.intValue) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.intValue;
		return result;
	}

	public int intValue() {
		return this.intValue;
	}
	
	public boolean isAvailableInTcpAt(final InetAddress bindAddr) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			// setReuseAddress(false) is required only on OSX, 
	        // otherwise the code will not work correctly on that platform 
			serverSocket.setReuseAddress(false);
			serverSocket.bind(new InetSocketAddress(bindAddr, this.intValue));
			return true;
		} catch (IOException e) {
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
	
	public boolean isAvailableInUdpAt(final InetAddress bindAddr) {
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(new InetSocketAddress(
					bindAddr, this.intValue));
			return true;
		} catch (SocketException e) {
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		return false;
	}

	public String toString() {
		return Integer.toString(this.intValue);
	}
}
