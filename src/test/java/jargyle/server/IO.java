package jargyle.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public final class IO {
	
	private static final int DEFAULT_READ_TIMEOUT = 60000; // 1 minute
	
	private static final String READ_TIMEOUT_PROPERTY_NAME = 
			"jargyle.server.io.readTimeout";
			
	private static final int HALF_SECOND = 500;
	
	private static Integer readTimeout;
	
	private static int getReadTimeout() {
		if (readTimeout == null) {
			String property = System.getProperty(READ_TIMEOUT_PROPERTY_NAME);
			if (property == null) {
				readTimeout = Integer.valueOf(DEFAULT_READ_TIMEOUT);
			} else {
				int timeout;
				try {
					timeout = Integer.parseInt(property);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(String.format(
							"%s must be a positive integer between 1 and %s (inclusive)",
							READ_TIMEOUT_PROPERTY_NAME,
							Integer.MAX_VALUE));
				}
				if (timeout < 1) {
					throw new IllegalArgumentException(String.format(
							"%s must be a positive integer between 1 and %s (inclusive)",
							READ_TIMEOUT_PROPERTY_NAME,
							Integer.MAX_VALUE));
				}
				readTimeout = Integer.valueOf(timeout);
			}
		}
		return readTimeout.intValue();
	}
	
	public static byte[] readFrom(final InputStream in) throws IOException {
		int available = 0;
		long startWaitTime = System.currentTimeMillis();
		long endWaitTime = System.currentTimeMillis();
		do {
			available = in.available();
			if (available > 0) {
				break;
			} else {
				endWaitTime = System.currentTimeMillis();
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} while ((endWaitTime - startWaitTime) < getReadTimeout());
		if (available == 0) {
			return new byte[] { };
		}
		byte[] bytes = new byte[available];
		int bytesRead = in.read(bytes);
		if (bytesRead == -1) {
			return new byte[] { };
		}
		return Arrays.copyOf(bytes, bytesRead);
	}

	public static void writeThenFlush(
			final byte[] b, final OutputStream out) throws IOException {
		out.write(b);
		out.flush();
	}

	private IO() { }
	
}
