package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class FilterDatagramSocket extends DatagramSocket {

	private final DatagramPacketFilter datagramPacketFilter;
	
	public FilterDatagramSocket(
			final DatagramPacketFilter filter) throws SocketException {
		this.datagramPacketFilter = filter;
	}

	public FilterDatagramSocket(
			final DatagramPacketFilter filter, 
			final int port) throws SocketException {
		super(port);
		this.datagramPacketFilter = filter;
	}

	public FilterDatagramSocket(
			final DatagramPacketFilter filter, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		super(port, laddr);
		this.datagramPacketFilter = filter;
	}

	public FilterDatagramSocket(
			final DatagramPacketFilter filter,
			final SocketAddress bindaddr) throws SocketException {
		super(bindaddr);
		this.datagramPacketFilter = filter;
	}
	
	public DatagramPacketFilter getDatagramPacketFilter() {
		return this.datagramPacketFilter;
	}
	
	@Override
	public synchronized void receive(
			final DatagramPacket p) throws IOException {
		super.receive(p);
		this.datagramPacketFilter.filterAfterReceive(p);
	}

	@Override
	public void send(final DatagramPacket p) throws IOException {
		this.datagramPacketFilter.filterBeforeSend(p);
		super.send(p);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [datagramPacketFilter=")
			.append(this.datagramPacketFilter)
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append("]");
		return builder.toString();
	}
	
}
