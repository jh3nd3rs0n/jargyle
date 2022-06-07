package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.github.jh3nd3rs0n.jargyle.common.net.FilterDatagramSocket;

final class BandwidthLimitedDatagramSocket extends FilterDatagramSocket {

	private static final long ONE_SECOND = 1000;
	
	private int byteCount;
	private final int limit;
	private Long timestamp;
	
	public BandwidthLimitedDatagramSocket(
			final DatagramSocket datagramSock, 
			final int bandwidthLimit) throws SocketException {
		super(datagramSock);
		this.byteCount = 0;
		this.limit = bandwidthLimit;
		this.timestamp = null;
	}
	
	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		if (this.timestamp == null) {
			this.timestamp = Long.valueOf(System.currentTimeMillis());
		}
		if (this.byteCount > this.limit) {
			long now = System.currentTimeMillis();
			if (this.timestamp.longValue() + ONE_SECOND >= now) {
				try {
					Thread.sleep(
							this.timestamp.longValue() + ONE_SECOND - now);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			this.byteCount = 0;
			this.timestamp = Long.valueOf(now);
		}
		this.datagramSocket.receive(p);
		this.byteCount += p.getLength();
	}
	
}