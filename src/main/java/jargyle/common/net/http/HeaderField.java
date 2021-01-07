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

public final class HeaderField {

	private static final class Params {
		private String fieldName;
		private String fieldValue;
		private byte[] byteArray;
	}
	
	public static HeaderField newInstance(final byte[] b) {
		HeaderField headerField = null;
		try {
			headerField = newInstanceFrom(new InputStreamReader(
					new ByteArrayInputStream(b), Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return headerField;
	}
	
	public static HeaderField newInstance(
			final String name, final String value) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		if (name.matches(StringConstants.ANY_WHITESPACE_CHAR_REGEX)) {
			throw new IllegalArgumentException(
					"field name must not contain any whitespace");
		}
		if (value.matches(StringConstants.UNACCEPTABLE_WHITESPACE_CHAR_REGEX)) {
			throw new IllegalArgumentException(
					"field value can only have space characters and horizontal "
					+ "tab characters");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(name);
			writer.write(":");
			writer.write(value);
			writer.write(StringConstants.CRLF);
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}		
		Params params = new Params();
		params.fieldName = name;
		params.fieldValue = value;
		params.byteArray = out.toByteArray();
		return new HeaderField(params);
	}
	
	public static HeaderField newInstanceFrom(
			final Reader reader) throws IOException {
		String string = IoHelper.readLineFrom(reader);
		if (!string.endsWith(StringConstants.CRLF)) {
			throw new IOException("expected CRLF");
		}
		if (string.equals(StringConstants.CRLF)) {
			return null;
		}
		String trimmedString = string.substring(
				0, string.length() - StringConstants.CRLF.length());
		String[] trimmedStringElements = trimmedString.split(":", 2);
		if (trimmedStringElements.length != 2) {
			throw new IOException("expected colon character");
		}
		String name = trimmedStringElements[0];
		if (name.matches(StringConstants.ANY_WHITESPACE_CHAR_REGEX)) {
			throw new IOException("field name must not contain any whitespace");
		}
		String value = trimmedStringElements[1];
		if (value.matches(StringConstants.UNACCEPTABLE_WHITESPACE_CHAR_REGEX)) {
			throw new IOException(
					"field value can only have space characters and horizontal "
					+ "tab characters");
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(name);
			writer.write(":");
			writer.write(value);
			writer.write(StringConstants.CRLF);
			writer.flush();
		}
		Params params = new Params();
		params.fieldName = name;
		params.fieldValue = value;
		params.byteArray = out.toByteArray();
		return new HeaderField(params);
	}
	
	private final String fieldName;
	private final String fieldValue;
	private final byte[] byteArray;
	
	private HeaderField(final Params params) {
		this.fieldName = params.fieldName;
		this.fieldValue = params.fieldValue;
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
		HeaderField other = (HeaderField) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [fieldName=")
			.append(this.fieldName)
			.append(", fieldValue=")
			.append(this.fieldValue)
			.append("]");
		return builder.toString();
	}
	
	
	
}
