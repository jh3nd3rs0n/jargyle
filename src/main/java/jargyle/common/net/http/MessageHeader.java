package jargyle.common.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MessageHeader<S extends StartLine> {

	private static final class Params<S> {
		private S startLine;
		private List<HeaderField> headerFields;
		private byte[] byteArray;
	}
	
	public static <S extends StartLine> MessageHeader<S> newInstance(
			final byte[] b, final Class<S> cls) {
		MessageHeader<S> messageHeader = null;
		if (cls.equals(RequestLine.class)) {
			try {
				@SuppressWarnings("unchecked")
				MessageHeader<S> messageHdr = 
						(MessageHeader<S>) newRequestInstanceFrom(new InputStreamReader(
								new ByteArrayInputStream(b), Charset.forName("UTF-8")));
				messageHeader = messageHdr;
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (cls.equals(StatusLine.class)) {
			try {
				@SuppressWarnings("unchecked")
				MessageHeader<S> messageHdr = 
						(MessageHeader<S>) newStatusInstanceFrom(new InputStreamReader(
								new ByteArrayInputStream(b), Charset.forName("UTF-8")));
				messageHeader = messageHdr;
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}			
		}
		return messageHeader;
	}
	
	private static <S extends StartLine> MessageHeader<S> newInstance(
			final S startLn, final List<HeaderField> headerFlds) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out.write(startLn.toByteArray());
			for (HeaderField headerFld : headerFlds) {
				out.write(headerFld.toByteArray());
			}
			out.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(StringConstants.CRLF);
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params<S> params = new Params<S>();
		params.startLine = startLn;
		params.headerFields = headerFlds;
		params.byteArray = out.toByteArray();
		return new MessageHeader<S>(params);
	}
	
	public static MessageHeader<RequestLine> newRequestInstance(
			final Method method, 
			final String requestTarget, 
			final String httpVersion,
			final HeaderField... headerFields) {
		return newInstance(
				RequestLine.newInstance(method, requestTarget, httpVersion), 
				Arrays.asList(headerFields));
	}
	
	public static MessageHeader<StatusLine> newStatusInstance(
			final String httpVersion,
			final int statusCode,
			final String reasonPhrase,
			final HeaderField... headerFields) {
		return newInstance(
				StatusLine.newInstance(httpVersion, statusCode, reasonPhrase),
				Arrays.asList(headerFields));
	}
	
	private static <S extends StartLine> MessageHeader<S> newInstanceFrom(
			final S startLn, final Reader reader) throws IOException {
		List<HeaderField> headerFlds = new ArrayList<HeaderField>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(startLn.toByteArray());
		HeaderField headerFld = null;
		while ((headerFld = HeaderField.newInstanceFrom(reader)) != null) {
			headerFlds.add(headerFld);
			out.write(headerFld.toByteArray());
		}
		out.flush();
		try (Writer writer = new OutputStreamWriter(
				out, Charset.forName("US-ASCII"))) {
			writer.write(StringConstants.CRLF);
			writer.flush();
		}
		Params<S> params = new Params<S>();
		params.startLine = startLn;
		params.headerFields = headerFlds;
		params.byteArray = out.toByteArray();		
		return new MessageHeader<S>(params);
	}
	
	public static MessageHeader<RequestLine> newRequestInstanceFrom(
			final Reader reader) throws IOException {
		RequestLine startLn = RequestLine.newInstanceFrom(reader);
		return newInstanceFrom(startLn, reader);
	}
	
	public static MessageHeader<StatusLine> newStatusInstanceFrom(
			final Reader reader) throws IOException {
		StatusLine startLn = StatusLine.newInstanceFrom(reader);
		return newInstanceFrom(startLn, reader);
	}
	
	private final S startLine;
	private final List<HeaderField> headerFields;
	private final byte[] byteArray;
	
	private MessageHeader(final Params<S> params) {
		this.startLine = params.startLine;
		this.headerFields = params.headerFields;
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
		@SuppressWarnings("unchecked")
		MessageHeader<S> other = (MessageHeader<S>) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public List<HeaderField> getHeaderFields() {
		return Collections.unmodifiableList(this.headerFields);
	}
	
	public List<String> getHeaderFieldValues(final String fieldName) {
		List<String> fieldValues = new ArrayList<String>();
		for (HeaderField headerField : this.headerFields) {
			if (headerField.getFieldName().equalsIgnoreCase(fieldName)) {
				fieldValues.add(headerField.getFieldValue());
			}
		}
		return Collections.unmodifiableList(fieldValues);
	}

	public String getLastHeaderFieldValue(final String fieldName) {
		String fieldValue = null;
		List<String> fieldValues = this.getHeaderFieldValues(fieldName);
		int fieldValuesSize = fieldValues.size(); 
		if (fieldValuesSize > 0) {
			fieldValue = fieldValues.get(fieldValuesSize - 1);
		}
		return fieldValue;
	}
	
	public S getStartLine() {
		return this.startLine;
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
			.append(" [startLine=")
			.append(this.startLine)
			.append(", headerFields=")
			.append(this.headerFields)
			.append("]");
		return builder.toString();
	}
	
}
