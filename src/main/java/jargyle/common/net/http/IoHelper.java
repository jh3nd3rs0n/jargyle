package jargyle.common.net.http;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

final class IoHelper {

	public static String readLineFrom(final Reader reader) throws IOException {
		StringWriter writer = new StringWriter();
		int ch = -1;
		while ((ch = reader.read()) != -1) {
			writer.write(ch);
			writer.flush();
			if (writer.toString().endsWith(StringConstants.CRLF)) {
				break;
			}
		}
		return writer.toString();
	}
	
	public static String readWordFrom(
			final Reader reader, 
			final char terminatingWhitespaceChar) throws IOException {
		if (!Character.isWhitespace(terminatingWhitespaceChar)) {
			throw new IllegalArgumentException(
					"character is not a terminating whitespace character");
		}
		StringWriter writer = new StringWriter();
		int ch = -1;
		while ((ch = reader.read()) != -1) {
			char c = (char) ch;
			if (Character.isWhitespace(c)) {
				if (terminatingWhitespaceChar == c) {
					writer.write(ch);
					break;
				} else {
					throw new IOException(
							"expected terminating whitespace character");
				}
			} else {
				writer.write(ch);
			}
		}
		return writer.toString();
	}
	
}
