package com.github.jh3nd3rs0n.jargyle.clientserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MeasuredIoHelper {
	
	private static final int MAX_BUFFER_LENGTH = 254;

	public static byte[] readFrom(final InputStream in) throws IOException {
		int bytesToRead = -1;
		boolean moreBytesToRead = false;
		ByteArrayOutputStream bytesOut = null;
		while (true) {
			if (bytesToRead == -1) {
				bytesToRead = in.read();
				if (bytesToRead == -1) {
					break;
				}
				if (bytesToRead == MAX_BUFFER_LENGTH + 1) {
					bytesToRead = MAX_BUFFER_LENGTH;
					moreBytesToRead = true;
				} else {
					moreBytesToRead = false;
				}
			}
			byte[] bytes = new byte[bytesToRead];
			int bytesRead = in.read(bytes);
			if (bytesRead == -1) {
				break;
			}
			if (bytesOut == null) {
				bytesOut = new ByteArrayOutputStream();
			}
			bytesOut.write(bytes, 0, bytesRead);
			bytesToRead = bytesToRead - bytesRead;
			if (bytesToRead == 0) {
				if (moreBytesToRead) {
					bytesToRead = -1;
				} else {
					break;
				}
			}
		}
		if (bytesOut == null) {
			return null;
		}
		return bytesOut.toByteArray();
	}

	public static void writeThenFlush(
			final byte[] b,
			final int off,
			final int len,
			final OutputStream out) throws IOException {
		if (len == 0) {
			out.write(len);
			out.flush();
			return;
		}
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
		int bufferLength = 0;
		for (int i = off; i < off + len; i++) {
			bufferOut.write(b[i]);
			if (++bufferLength == MAX_BUFFER_LENGTH) {
				if (i < off + len - 1) {
					bufferLength++;
				}
				bytesOut.write(bufferLength);
				bytesOut.write(bufferOut.toByteArray());
				bufferOut = new ByteArrayOutputStream();
				bufferLength = 0;
			}
		}
		if (bufferLength > 0) {
			bytesOut.write(bufferLength);
			bytesOut.write(bufferOut.toByteArray());
		}
		byte[] bytes = bytesOut.toByteArray();
		if (bytes.length == 0) {
			out.write(bytes.length);
		}
		out.write(bytes);
		out.flush();		
	}

	public static void writeThenFlush(
			final byte[] b, final OutputStream out) throws IOException {
		writeThenFlush(b, 0, b.length, out);
	}

	private MeasuredIoHelper() { }
	
}
