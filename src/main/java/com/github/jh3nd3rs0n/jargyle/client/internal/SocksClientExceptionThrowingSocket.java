package com.github.jh3nd3rs0n.jargyle.client.internal;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.FilterSocket;

public final class SocksClientExceptionThrowingSocket extends FilterSocket {

	private static final class SocksClientExceptionThrowingSocketInputStream 
		extends FilterInputStream {

		private final SocksClient socksClient;
		
		public SocksClientExceptionThrowingSocketInputStream(
				final SocksClient client, final InputStream inStream) {
			super(inStream);
			this.socksClient = client;
		}

		@Override
		public int available() throws IOException {
			int available = 0;
			try {
				available = this.in.available();
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
			return available;
		}
		
		@Override
		public void close() throws IOException {
			try {
				this.in.close();
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}		
		}
		
		@Override
		public int read() throws IOException {
			int b = -1;
			try {
				b = this.in.read();
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
			return b;
		}
		
		@Override
		public int read(byte[] b) throws IOException {
			int bytesRead = -1;
			try {
				bytesRead = this.in.read(b);
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
			return bytesRead;
		}
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int bytesRead = -1;
			try {
				bytesRead = this.in.read(b, off, len);
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
			return bytesRead;
		}
	}

	private static final class SocksClientExceptionThrowingSocketOutputStream 
		extends FilterOutputStream {

		private final SocksClient socksClient;
		
		public SocksClientExceptionThrowingSocketOutputStream(
				final SocksClient client, final OutputStream outStream) {
			super(outStream);
			this.socksClient = client;
		}

		@Override
		public synchronized void close() throws IOException {
			try {
				this.out.close();
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
		}
		
		@Override
		public void flush() throws IOException {
			try {
				this.out.flush();
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			try {
				this.out.write(b);
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			try {
				this.out.write(b, off, len);
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}			
		}
		
		@Override
		public void write(final int b) throws IOException {
			try {
				this.out.write(b);
			} catch (IOException e) {
				SocksClientExceptionThrowingHelper.throwAsSocksClientException(
						e, this.socksClient);
			}
		}
	}
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private SocksClient socksClient;
	
	public SocksClientExceptionThrowingSocket(
			final SocksClient client, final Socket sock) {
		super(sock);
		this.inputStream = null;
		this.outputStream = null;
		this.socksClient = client;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if (this.inputStream != null) {
			return this.inputStream;
		}
		this.inputStream = new SocksClientExceptionThrowingSocketInputStream(
				this.socksClient, super.getInputStream());
		return this.inputStream;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		if (this.outputStream != null) {
			return this.outputStream;
		}
		this.outputStream = new SocksClientExceptionThrowingSocketOutputStream(
				this.socksClient, super.getOutputStream());
		return this.outputStream;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socksClient=")
			.append(this.socksClient)
			.append(", socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}

}
