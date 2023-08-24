package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.internal.net.FilterSocket;

final class GssSocket extends FilterSocket {

	private static final class GssUnwrappedInputStream extends InputStream {
		
		private ByteArrayInputStream bufferIn;
		private boolean closed;
		private final GSSContext gssContext;
		private final InputStream in;
		
		public GssUnwrappedInputStream(
				final GSSContext context, 
				final InputStream inStream) {
			this.bufferIn = new ByteArrayInputStream(new byte[] { });
			this.closed = false;
			this.gssContext = context;
			this.in = inStream;
		}
		
		@Override
		public int available() throws IOException {
			if (this.closed) {
				throw new IOException("stream closed");
			}
			int bufferInAvailable = this.bufferIn.available(); 
			if (bufferInAvailable > 0) {
				return bufferInAvailable;
			}
			return this.in.available();
		}

		@Override
		public void close() throws IOException {
			this.bufferIn = new ByteArrayInputStream(new byte[] { });
			this.in.close();
			this.closed = true;			
		}
		
		@Override
		public int read() throws IOException {
			if (this.closed) { return -1; }
			if (this.bufferIn.available() == 0) {
				int b = this.in.read();
				if (b == -1) {
					this.closed = true;
					return b;
				}
				Message message = Message.newInstanceFrom(
						new SequenceInputStream(
								new ByteArrayInputStream(
										new byte[] { (byte) b }),
								this.in));
				byte[] token = message.getToken();
				MessageProp prop = new MessageProp(0, false);
				try {
					token = this.gssContext.unwrap(
							token, 0, token.length, prop);
				} catch (GSSException e) {
					throw new IOException(e);
				}
				this.bufferIn = new ByteArrayInputStream(token);				
			}
			return this.bufferIn.read();
		}
		
		@Override
		public int read(byte[] b) throws IOException {
			return this.read(b, 0, b.length);
		}
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			Objects.requireNonNull(b);
			if (off < 0) {
				throw new IndexOutOfBoundsException(String.format(
						"offset is negative: %s", 
						off));
			}
			if (len < 0) {
				throw new IndexOutOfBoundsException(String.format(
						"specified length is negative: %s", 
						len));
			}
			if (len > b.length - off) {
				throw new IndexOutOfBoundsException(String.format(
						"specified length is greater than "
						+ "the length of the array minus offset: "
						+ "len (%s) > length of array (%s) - off (%s)", 
						len,
						b.length,
						off));
			}
			if (this.closed) { return -1; }
			int offset = off;
			int bufferInAvailable = this.bufferIn.available();
			if (bufferInAvailable == 0) {
				int by = this.read();
				if (by == -1) {
					return by;
				}
				b[offset] = (byte) by;
				offset++;
				bufferInAvailable = 1 + this.bufferIn.available();
			}
			int length = len;
			if (length > bufferInAvailable) {
				length = bufferInAvailable;
			}
			for (int i = offset; i < off + length; i++) {
				b[i] = (byte) this.bufferIn.read();
			}
			return length;
		}
		
	}
	
	private static final class GssWrappedOutputStream extends OutputStream {
		
		private ByteArrayOutputStream bufferOut;
		private int bufferOutLength;
		private final GSSContext gssContext;
		private final MessageProp messageProp;
		private final OutputStream out;
		private final int wrapSizeLimit;
		
		public GssWrappedOutputStream(
				final GSSContext context, 
				final MessageProp prop, 
				final OutputStream outStream) {
			int sizeLimit;
			try {
				sizeLimit = context.getWrapSizeLimit(
						prop.getQOP(), 
						prop.getPrivacy(), 
						Message.MAX_TOKEN_LENGTH);
			} catch (GSSException e) {
				throw new AssertionError(e);
			}
			this.bufferOut = new ByteArrayOutputStream();
			this.bufferOutLength = 0;
			this.gssContext = context;
			this.messageProp = new MessageProp(
					prop.getQOP(), prop.getPrivacy());
			this.out = outStream;
			this.wrapSizeLimit = sizeLimit;
		}

		@Override
		public synchronized void close() throws IOException {
			this.bufferOut = new ByteArrayOutputStream();
			this.bufferOutLength = 0;
			this.out.close();
		}
		
		@Override
		public void flush() throws IOException {
			byte[] bytes = this.bufferOut.toByteArray();
			MessageProp prop = new MessageProp(
					this.messageProp.getQOP(), 
					this.messageProp.getPrivacy());
			byte[] token;
			try {
				token = this.gssContext.wrap(
						bytes, 0, bytes.length, prop);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			this.out.write(Message.newInstance(
					MessageType.ENCAPSULATED_USER_DATA, 
					token).toByteArray());
			this.out.flush();
			this.bufferOut = new ByteArrayOutputStream();
			this.bufferOutLength = 0;
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			this.write(b, 0, b.length);
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			Objects.requireNonNull(b);
			if (off < 0) {
				throw new IndexOutOfBoundsException(String.format(
						"offset is negative: %s", 
						off));
			}
			if (len < 0) {
				throw new IndexOutOfBoundsException(String.format(
						"specified length is negative: %s", 
						len));
			}
			if (off + len > b.length) {
				throw new IndexOutOfBoundsException(String.format(
						"offset and specified length is greater than "
						+ "the length of the array: "
						+ "off (%s) + len (%s) > length of array (%s)", 
						off,
						len,
						b.length));
			}
			for (int i = off; i < off + len; i++) {
				this.write(b[i]);
			}
		}
		
		@Override
		public void write(int b) throws IOException {
			this.bufferOut.write(b);
			if (++this.bufferOutLength == this.wrapSizeLimit) {
				this.flush();
			}
		}
			
	}

	private final GSSContext gssContext;
	private InputStream inputStream;
	private MessageProp messageProp;
	private OutputStream outputStream;
	
	public GssSocket(
			final Socket sock,
			final GSSContext context,
			final MessageProp prop) {
		super(sock);
		MessageProp prp = null;
		if (prop != null) {
			prp = new MessageProp(prop.getQOP(), prop.getPrivacy());
		}
		this.gssContext = context;
		this.messageProp = prp;
		this.inputStream = null;
		this.outputStream = null;		
	}

	@Override
	public synchronized void close() throws IOException {
		try {
			this.gssContext.dispose();
		} catch (GSSException e) {
			throw new IOException(e);
		}
		this.socket.close();
	}

	@Override
	public SocketChannel getChannel() {
		throw new UnsupportedOperationException();
	}
	
	public GSSContext getGSSContext() {
		return this.gssContext;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if (this.inputStream != null) {
			return this.inputStream;
		}
		InputStream inStream = this.socket.getInputStream();
		if (this.messageProp == null) {
			this.inputStream = inStream;
		} else {
			this.inputStream = new GssUnwrappedInputStream(
					this.gssContext, inStream);
		}
		return this.inputStream;
	}
	
	public MessageProp getMessageProp() {
		if (this.messageProp == null) {
			return this.messageProp;
		}
		return new MessageProp(
				this.messageProp.getQOP(), this.messageProp.getPrivacy());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if (this.outputStream != null) {
			return this.outputStream;
		}
		OutputStream outStream = this.socket.getOutputStream();
		if (this.messageProp == null) {
			this.outputStream = outStream;
		} else {
			this.outputStream = new GssWrappedOutputStream(
					this.gssContext, this.messageProp, outStream);
		}
		return this.outputStream;		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [gssContext=")
			.append(this.gssContext)
			.append(", messageProp=")
			.append(this.messageProp)
			.append(", socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}
	
}
