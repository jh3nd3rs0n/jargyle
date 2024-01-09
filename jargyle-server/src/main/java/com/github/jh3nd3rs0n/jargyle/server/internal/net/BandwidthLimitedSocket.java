package com.github.jh3nd3rs0n.jargyle.server.internal.net;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.net.FilterSocket;

public final class BandwidthLimitedSocket extends FilterSocket {
	
	private static final class BandwidthLimitedInputStream 
		extends FilterInputStream {

		private static final long ONE_SECOND = 1000;

		private int byteCount;
		private final int limit;
		private Long timestamp;

		public BandwidthLimitedInputStream(
				final InputStream inStream, final int bandwidthLimit) {
			super(inStream);
			this.byteCount = 0;
			this.limit = bandwidthLimit;
			this.timestamp = null;
		}

		@Override
		public int read() throws IOException {
			byte[] b = new byte[1];
			int newLength = this.read(b);
			if (newLength == -1) {
				return -1;
			}
			return UnsignedByte.newInstanceOf(b[0]).intValue();
		}

		@Override
		public int read(byte[] b) throws IOException {
			return this.read(b, 0, b.length);
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
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
			int newLength = this.in.read(b, off, len);
			if (newLength >= 0) {
				this.byteCount += newLength;
			}
			return newLength;
		}

	}

	private InputStream inputStream;
	private final int limit;
	
	public BandwidthLimitedSocket(final Socket sock, final int bandwidthLimit) {
		super(sock);
		this.inputStream = null;
		this.limit = bandwidthLimit;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (this.inputStream != null) {
			return this.inputStream;
		}
		InputStream inStream = this.socket.getInputStream();
		this.inputStream = new BandwidthLimitedInputStream(
				inStream, this.limit);
		return this.inputStream;
	}

}
