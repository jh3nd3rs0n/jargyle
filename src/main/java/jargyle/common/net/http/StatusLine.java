package jargyle.common.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class StatusLine extends StartLine {

	private static final class Params {
		private String httpVersion;
		private int statusCode;
		private String reasonPhrase;
		private byte[] byteArray;
	}
	
	private static final int MAX_STATUS_CODE = 999;
	
	private static final int MIN_STATUS_CODE = 100;

	public static StatusLine newInstance(final byte[] b) {
		StatusLine statusLine = null;
		try {
			statusLine = newInstanceFrom(new InputStreamReader(
					new ByteArrayInputStream(b), Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return statusLine;
	}

	public static StatusLine newInstance(
			final String httpVer, final int status, final String reason) {
		String ver = httpVer;
		if (ver == null) {
			ver = StringConstants.HTTP_VERSION;
		}
		if (!ver.matches(StringConstants.HTTP_VERSION_REGEX)) {
			throw new IllegalArgumentException(String.format(
					"invalid HTTP version: '%s'", ver));
		}
		if (status < MIN_STATUS_CODE || status > MAX_STATUS_CODE) {
			throw new IllegalArgumentException(String.format(
					"status code must be at least %s and no more than %s. "
					+ "actual status code: '%s'", 
					MIN_STATUS_CODE,
					MAX_STATUS_CODE,
					status));
		}
		if (reason != null && reason.matches(
				StringConstants.UNACCEPTABLE_WHITESPACE_CHAR_REGEX)) {
			throw new IllegalArgumentException(
					"reason phrase can only have space characters and "
					+ "horizontal tab characters");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(ver);
			writer.write(" ");
			writer.write(Integer.toString(status));
			writer.write(" ");
			if (reason != null) {
				writer.write(reason);
			}
			writer.write(StringConstants.CRLF);
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.httpVersion = ver;
		params.statusCode = status;
		params.reasonPhrase = reason;
		params.byteArray = out.toByteArray();
		return new StatusLine(params);
	}
	
	public static StatusLine newInstanceFrom(
			final Reader reader) throws IOException {
		String httpVer = null;
		int status = -1;
		String reason = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			String string = IoHelper.readWordFrom(reader, ' ');
			String trimmedString = string.trim();
			if (trimmedString.length() == 0) {
				throw new IOException("expected HTTP version");
			}
			if (!trimmedString.matches(StringConstants.HTTP_VERSION_REGEX)) {
				throw new IOException(String.format(
						"invalid HTTP version: '%s'", trimmedString));
			}
			httpVer = trimmedString;
			if (string.length() -  trimmedString.length() == 0) {
				throw new IOException("expected single space character");
			}
			writer.write(string);
			string = IoHelper.readWordFrom(reader, ' ');
			trimmedString = string.trim();
			if (trimmedString.length() == 0) {
				throw new IOException("expected status code");
			}
			try {
				status = Integer.valueOf(trimmedString);
			} catch (NumberFormatException e) {
				throw new IOException(String.format(
						"status code must be an integer of at least %s and "
						+ "no more than %s. actual status code: '%s'",
						MIN_STATUS_CODE,
						MAX_STATUS_CODE,
						trimmedString));
			}
			if (string.length() -  trimmedString.length() == 0) {
				throw new IOException("expected single space character");
			}
			writer.write(string);
			string = IoHelper.readLineFrom(reader);
			if (!string.endsWith(StringConstants.CRLF)) {
				throw new IOException(
						"expected string ending with a carriage return "
						+ "character followed by a line feed character");
			}
			trimmedString = string.substring(
					0, string.length() - StringConstants.CRLF.length());
			if (trimmedString.matches(
					StringConstants.UNACCEPTABLE_WHITESPACE_CHAR_REGEX)) {
				throw new IOException(
						"reason phrase can only have space characters and "
						+ "horizontal tab characters");
			}
			reason = trimmedString;
			writer.write(string);
			writer.flush();
		}
		Params params = new Params();
		params.httpVersion = httpVer;
		params.statusCode = status;
		params.reasonPhrase = reason;
		params.byteArray = out.toByteArray();		
		return new StatusLine(params);
	}
	
	private final String httpVersion;
	private final int statusCode;
	private final String reasonPhrase;
	private final byte[] byteArray;
	
	private StatusLine(final Params params) {
		this.httpVersion = params.httpVersion;
		this.statusCode = params.statusCode;
		this.reasonPhrase = params.reasonPhrase;
		this.byteArray = params.byteArray;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		StatusLine other = (StatusLine) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public String getHttpVersion() {
		return this.httpVersion;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	@Override
	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [httpVersion=")
			.append(this.httpVersion)
			.append(", statusCode=")
			.append(this.statusCode)
			.append(", reasonPhrase=")
			.append(this.reasonPhrase)
			.append("]");
		return builder.toString();
	}

}
