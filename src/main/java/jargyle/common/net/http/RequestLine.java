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
import java.util.Objects;

public final class RequestLine extends StartLine {

	private static final class Params {
		private Method method;
		private String requestTarget;
		private String httpVersion;
		private byte[] byteArray;
	}

	public static RequestLine newInstance(final byte[] b) {
		RequestLine requestLine = null;
		try {
			requestLine = newInstanceFrom(new InputStreamReader(
					new ByteArrayInputStream(b), Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return requestLine;
	}
	
	public static RequestLine newInstance(
			final Method meth, final String reqTarget, final String httpVer) {
		Objects.requireNonNull(meth);
		Objects.requireNonNull(reqTarget);
		String ver = httpVer;
		if (ver == null) {
			ver = httpVer;
		}
		if (reqTarget.length() == 0) {
			throw new IllegalArgumentException(
					"length of request target must be at least 1");
		}
		if (reqTarget.matches(StringConstants.ANY_WHITESPACE_CHAR_REGEX)) {
			throw new IllegalArgumentException(
					"request target must not contain any whitespace");
		}
		if (!ver.matches(StringConstants.HTTP_VERSION_REGEX)) {
			throw new IllegalArgumentException(String.format(
					"invalid HTTP version: '%s'", ver));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(meth.toString());
			writer.write(" ");
			writer.write(reqTarget);
			writer.write(" ");
			writer.write(ver);
			writer.write(StringConstants.CRLF);
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.method = meth;
		params.requestTarget = reqTarget;
		params.httpVersion = ver;
		params.byteArray = out.toByteArray();
		return new RequestLine(params);
	}
	
	public static RequestLine newInstanceFrom(
			final Reader reader) throws IOException {
		Method meth = null;
		String reqTarget = null;
		String httpVer = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			String string = IoHelper.readWordFrom(reader, ' ');
			String trimmedString = string.trim(); 
			try {
				meth = Method.getInstance(trimmedString);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			if (string.length() -  trimmedString.length() == 0) {
				throw new IOException("expected space character");
			}
			writer.write(string);
			string = IoHelper.readWordFrom(reader, ' ');
			trimmedString = string.trim();
			if (trimmedString.length() == 0) {
				throw new IOException(
						"expected length of request target to be at least 1");
			}
			if (trimmedString.matches(
					StringConstants.ANY_WHITESPACE_CHAR_REGEX)) {
				throw new IllegalArgumentException(
						"request target must not contain any whitespace");
			}			
			reqTarget = trimmedString;
			if (string.length() -  trimmedString.length() == 0) {
				throw new IOException("expected space character");
			}
			writer.write(string);
			string = IoHelper.readWordFrom(reader, '\r');
			trimmedString = string.trim();
			if (trimmedString.length() == 0) {
				throw new IOException("expected HTTP version");
			}
			if (!trimmedString.matches(StringConstants.HTTP_VERSION_REGEX)) {
				throw new IOException(String.format(
						"invalid HTTP version: '%s'", trimmedString));
			}
			httpVer = trimmedString;
			if (string.length() -  trimmedString.length() == 0) {
				throw new IOException("expected carriage return character");
			}
			writer.write(string);
			int ch = reader.read();
			if (ch == -1 || ((char) ch) != '\n') {
				throw new IOException("expected line feed character");
			}
			writer.write(ch);
			writer.flush();
		}		
		Params params = new Params();
		params.method = meth;
		params.requestTarget = reqTarget;
		params.httpVersion = httpVer;
		params.byteArray = out.toByteArray();		
		return new RequestLine(params);
	}
	
	private final Method method;
	private final String requestTarget;
	private final String httpVersion;
	private final byte[] byteArray;
	
	private RequestLine(final Params params) {
		this.method = params.method;
		this.requestTarget = params.requestTarget;
		this.httpVersion = params.httpVersion;
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
		RequestLine other = (RequestLine) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}
	
	public String getHttpVersion() {
		return this.httpVersion;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public String getRequestTarget() {
		return this.requestTarget;
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
			.append(" [method=")
			.append(this.method)
			.append(", requestTarget=")
			.append(this.requestTarget)
			.append(", httpVersion=")
			.append(this.httpVersion)
			.append("]");
		return builder.toString();
	}

}
