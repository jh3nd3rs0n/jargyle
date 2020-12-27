package jargyle.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class IoHelper {

	private static final int MAX_BUFFER_SIZE = 255;
	
	public static byte[] readFrom(final InputStream in) throws IOException {
		int b = -1;
		b = in.read();
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
		if (b.length > MAX_BUFFER_SIZE) {
			throw new IllegalArgumentException(String.format(
					"buffer size must be no larger than %s byte(s). "
					+ "actual size is %s byte(s)", 
					MAX_BUFFER_SIZE,
					b.length));
		}
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		bytesOut.write(b.length);
		bytesOut.write(b);
		out.write(bytesOut.toByteArray());
		out.flush();
	}

	private IoHelper() { }
	
}
