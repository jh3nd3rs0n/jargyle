package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class CsvFileReader {
	
	private int chr;
	private int escapedTextEndIndex;
	private int escapedTextStartIndex;
	private String field;	
	private int index;
	private boolean isAtEndOfEscapedText;	
	private boolean isImmediatelyAfterEscapedText;
	private boolean isWithinEscapedText;
	private boolean isWithinNonescapedText;
	private boolean isWithinNonescapedTextOrImmediatelyAfterEscapedText;
	private String lineSeparator;	
	private final Reader reader;
	private StringBuilder stringBuilder;
	
	public CsvFileReader(final Reader rdr) {
		this.reader = rdr;
		this.initialize();
	}
	
	private void createStringBuilderIfNotCreated() {
		if (this.stringBuilder == null) {
			this.stringBuilder = new StringBuilder();
		}
	}
	
	private void initialize() {
		this.chr = -1;
		this.escapedTextEndIndex = -1;
		this.escapedTextStartIndex = -1;
		this.field = null;
		this.index = -1;
		this.isAtEndOfEscapedText = false;		
		this.isImmediatelyAfterEscapedText = false;
		this.isWithinEscapedText = false;		
		this.isWithinNonescapedText = false;
		this.isWithinNonescapedTextOrImmediatelyAfterEscapedText = false;
		this.lineSeparator = null;
		this.stringBuilder = null;
	}
	
	private void onDoubleQuoteCharacter() throws IOException {
		if (this.isWithinNonescapedText && this.index > 0) {
			throw new CsvFileReaderException(
					"field containing a double quote character ('\"') "
					+ "must be escaped in double quotes and the double "
					+ "quote character inside the field must be escaped by "
					+ "preceding it with another double quote character");
		} else if (this.index == 0) {
			this.escapedTextStartIndex = this.index;
			this.updateConditions();
		} else if (this.isWithinEscapedText) {
			this.escapedTextEndIndex = this.index;
			this.updateConditions();
		} else if (this.isImmediatelyAfterEscapedText) {
			this.stringBuilder.replace(
					this.escapedTextEndIndex, this.index, "");
			this.escapedTextEndIndex = -1;
			this.index--;
			this.updateConditions();
		}
	}
	
	private void onEndOfReader() throws IOException {
		if (this.isWithinEscapedText) {
			throw new CsvFileReaderException(
					"missing closing double quote character ('\"')");
		}
		this.lineSeparator = null;
		if (this.stringBuilder == null) {
			this.field = null;
		} else {
			if (this.isAtEndOfEscapedText) {
				String substring = this.stringBuilder.substring(
						this.escapedTextStartIndex + 1, 
						this.escapedTextEndIndex);
				this.stringBuilder.replace(
						this.escapedTextStartIndex, 
						this.escapedTextEndIndex + 1, 
						substring);				
			}
			this.field = this.stringBuilder.toString();
			this.stringBuilder.delete(0, this.stringBuilder.length());
			this.stringBuilder = null;
		}
		this.escapedTextStartIndex = -1;
		this.escapedTextEndIndex = -1;
		this.index = -1;
		this.updateConditions();
	}
	
	private void onNonescapedCarriageReturnCharacter() throws IOException {
		char ch = (char) this.chr;
		if ((this.chr = this.reader.read()) != -1) {
			char c = (char) this.chr;
			if (c == '\n') {
				this.lineSeparator = new String(new char[] { ch, c });
				this.removeEnclosingDoubleQuoteCharacters();						
				this.field = this.stringBuilder.toString();
				this.stringBuilder.delete(0, this.stringBuilder.length());
				this.stringBuilder = null;
				return;
			}
		}
		throw new CsvFileReaderException(
				"field containing carriage return character ('\\r') "
				+ "must be escaped in double quotes or the carriage "
				+ "return character must be followed by a line feed "
				+ "character ('\\n')");		
	}
	
	private void onNonescapedCommaCharacter() throws IOException {
		this.lineSeparator = null;
		this.removeEnclosingDoubleQuoteCharacters();
		this.field = this.stringBuilder.toString();
		this.stringBuilder.delete(0, this.stringBuilder.length());
	}
	
	private void onNonescapedLineFeedCharacter() throws IOException {
		char ch = (char) this.chr;
		this.lineSeparator = new String(new char[] { ch });
		this.removeEnclosingDoubleQuoteCharacters();						
		this.field = this.stringBuilder.toString();
		this.stringBuilder.delete(0, this.stringBuilder.length());
		this.stringBuilder = null;
	}
	
	private void read() throws IOException {
		this.initialize();
		while ((this.chr = this.reader.read()) != -1) {
			this.createStringBuilderIfNotCreated();
			this.index++;
			this.updateConditions();
			char ch = (char) this.chr;
			if (ch == ',' && this.isWithinNonescapedTextOrImmediatelyAfterEscapedText) {
				this.onNonescapedCommaCharacter();
				return;
			} else if (ch == '\r' && this.isWithinNonescapedTextOrImmediatelyAfterEscapedText) {
				this.onNonescapedCarriageReturnCharacter();
				return;
			} else if (ch == '\n' && this.isWithinNonescapedTextOrImmediatelyAfterEscapedText) {
				this.onNonescapedLineFeedCharacter();
				return;
			} else {
				if (ch == '\"') {
					this.onDoubleQuoteCharacter();
				}
				this.stringBuilder.append(ch);
			}
		}
		this.onEndOfReader();		
	}
	
	public List<String> readRecord() throws IOException {
		List<String> fields = new ArrayList<String>();
		do {
			this.read();
			if (this.field != null) {
				fields.add(this.field);
			}
		} while (this.field != null && this.lineSeparator == null);
		return Collections.unmodifiableList(fields);
	}
	
	private void removeEnclosingDoubleQuoteCharacters() {
		if (this.isImmediatelyAfterEscapedText) {
			String substring = this.stringBuilder.substring(
					this.escapedTextStartIndex + 1, this.escapedTextEndIndex);
			this.stringBuilder.replace(
					this.escapedTextStartIndex, 
					this.escapedTextEndIndex + 1, 
					substring);
			this.escapedTextStartIndex = -1;
			this.escapedTextEndIndex = -1;
			this.index -= 2;
			this.updateConditions();
		}
	}
	
	private void updateConditions() {
		this.isAtEndOfEscapedText = this.escapedTextStartIndex > -1 
				&& this.escapedTextEndIndex > -1
				&& this.escapedTextEndIndex == this.index;
		this.isImmediatelyAfterEscapedText = this.escapedTextStartIndex > -1 
				&& this.escapedTextEndIndex > -1
				&& this.escapedTextEndIndex == this.index - 1;
		this.isWithinEscapedText = this.escapedTextStartIndex > -1 
				&& this.escapedTextEndIndex == -1;
		this.isWithinNonescapedText = this.escapedTextStartIndex == -1
				&& this.escapedTextEndIndex == -1;
		this.isWithinNonescapedTextOrImmediatelyAfterEscapedText = 
				this.isWithinNonescapedText	
				|| this.isImmediatelyAfterEscapedText;
	}

}
