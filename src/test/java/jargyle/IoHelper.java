package jargyle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public final class IoHelper {

	private static final int MAX_BUFFER_LENGTH = 255;
	
	public static byte[] readFrom(final InputStream in) throws IOException {
		int bytesToRead = -1;
		ByteArrayOutputStream bytesOut = null;
		while (true) {
			if (bytesToRead == -1) {
				bytesToRead = in.read();
				if (bytesToRead == -1) {
					break;
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
				break;
			}
		}
		if (bytesOut == null) {
			return null;
		}
		return bytesOut.toByteArray();
	}

	public static String readStringFrom(final File file) throws IOException {
		String string = null;
		try (Reader reader = new InputStreamReader(
				new FileInputStream(file), Charset.forName("UTF-8"))) {
			string = readStringFrom(reader);
		}
		return string;
	}
	
	public static String readStringFrom(
			final Reader reader) throws IOException {
		String string = null;
		try (StringWriter writer = new StringWriter()) {
			int ch = -1;
			while ((ch = reader.read()) != -1) {
				writer.write(ch);
			}
			writer.flush();
			string = writer.toString();
		}
		return string;
	}
	
	public static void writeStringThenFlush(
			final String string, final Writer writer) throws IOException {
		writer.write(string);
		writer.flush();
	}
	
	public static void writeStringToFile(
			final String string, final File file) throws IOException {
		try (Writer writer = new OutputStreamWriter(
				new FileOutputStream(file), Charset.forName("UTF-8"))) {
			writeStringThenFlush(string, writer);
		}
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

	private IoHelper() { }
	
}
