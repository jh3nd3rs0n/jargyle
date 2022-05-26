package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

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
	private final SocksClient socksClient;
	
	public SocksClientExceptionThrowingSocket(
			final SocksClient client, final Socket sock) {
		super(sock);
		this.inputStream = null;
		this.outputStream = null;
		this.socksClient = client;
	}

	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		try {
			this.socket.bind(bindpoint);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public synchronized void close() throws IOException {
		try {
			this.socket.close();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		try {
			this.socket.connect(endpoint);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public void connect(SocketAddress endpoint, int timeout) throws IOException {
		try {
			this.socket.connect(endpoint, timeout);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (this.inputStream != null) {
			return this.inputStream;
		}
		InputStream inStream = null;
		try {
			inStream = this.socket.getInputStream();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
		this.inputStream = new SocksClientExceptionThrowingSocketInputStream(
				this.socksClient, inStream);
		return this.inputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (this.outputStream != null) {
			return this.outputStream;
		}
		OutputStream outStream = null;
		try {
			outStream = this.socket.getOutputStream();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
		this.outputStream = new SocksClientExceptionThrowingSocketOutputStream(
				this.socksClient, outStream);
		return this.outputStream;
	}

	@Override
	public void sendUrgentData(int data) throws IOException {
		try {
			this.socket.sendUrgentData(data);
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}
	
	@Override
	public void shutdownInput() throws IOException {
		try {
			this.socket.shutdownInput();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
	}
	
	@Override
	public void shutdownOutput() throws IOException {
		try {
			this.socket.shutdownOutput();
		} catch (IOException e) {
			SocksClientExceptionThrowingHelper.throwAsSocksClientException(
					e, this.socksClient);
		}
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
