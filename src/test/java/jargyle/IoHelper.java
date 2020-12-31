package jargyle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class IoHelper {

	private static final int MAX_BUFFER_LENGTH = 255;
	
	public static byte[] readFrom(final InputStream in) throws IOException {
		int b = in.read();
		if (b == -1 || b == 0) {
			return new byte[] { };
		}
		byte[] bytes = new byte[b];
		int bytesRead = in.read(bytes);
		if (bytesRead == -1) {
			return new byte[] { };
		}
		return Arrays.copyOf(bytes, bytesRead);
	}

	public static void writeThenFlush(
			final byte[] b, final OutputStream out) throws IOException {
		if (b.length == 0) {
			out.write(b.length);
			out.flush();
			return;
		}
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
		int bufferSize = 0;
		for (int i = 0; i < b.length; i++) {
			int byt = b[i];
			if (byt != -1) {
				bufferOut.write(byt);
				if (++bufferSize == MAX_BUFFER_LENGTH) {
					bytesOut.write(bufferSize);
					bytesOut.write(bufferOut.toByteArray());
					bufferOut = new ByteArrayOutputStream();
					bufferSize = 0;
				}
			}
		}
		if (bufferSize > 0) {
			bytesOut.write(bufferSize);
			bytesOut.write(bufferOut.toByteArray());
		}
		byte[] bytes = bytesOut.toByteArray();
		if (bytes.length == 0) {
			out.write(bytes.length);
		}
		out.write(bytes);
		out.flush();
	}

	private IoHelper() { }
	
}
