package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class CsvParser {
	
	private int chr;
	private int escapedEndIndex;
	private int escapedStartIndex;
	private int index;
	private boolean isAfterEscaped;	
	private boolean isEscaped;	
	private boolean isImmediatelyAfterEscaped;
	private boolean isNonescaped;
	private boolean isNonescapedOrImmediatelyAfterEscaped;
	private String lineSeparator;	
	private final Reader reader;
	private StringBuilder stringBuilder;
	private String value;
	
	public CsvParser(final Reader rdr) {
		this.reader = rdr;
		this.initialize();
	}
	
	private void createStringBuilderIfNotCreated() {
		if (this.stringBuilder == null) {
			this.stringBuilder = new StringBuilder();
		}
	}
	
	public String getLineSeparator() {
		return this.lineSeparator;
	}
	
	public String getValue() {
		return this.value;
	}
	
	private void initialize() {
		this.chr = -1;
		this.escapedEndIndex = -1;
		this.escapedStartIndex = -1;
		this.index = -1;
		this.isAfterEscaped = false;		
		this.isEscaped = false;		
		this.isImmediatelyAfterEscaped = false;		
		this.isNonescaped = false;
		this.isNonescapedOrImmediatelyAfterEscaped = false;
		this.lineSeparator = null;
		this.stringBuilder = null;
		this.value = null;
	}
	
	public void next() throws IOException {
		this.initialize();
		while ((this.chr = this.reader.read()) != -1) {
			this.createStringBuilderIfNotCreated();
			this.index++;
			this.updateEscapeConditions();
			char ch = (char) this.chr;
			if (ch == ',' && this.isNonescapedOrImmediatelyAfterEscaped) {
				this.onNonescapedCommaCharacter();
				return;
			} else if (ch == '\r' && this.isNonescapedOrImmediatelyAfterEscaped) {
				this.onNonescapedCarriageReturnCharacter();
				return;
			} else if (ch == '\n' && this.isNonescapedOrImmediatelyAfterEscaped) {
				this.onNonescapedLineFeedCharacter();	
			} else {
				if (ch == '\"') {
					this.onDoubleQuoteCharacter();
				} else {
					this.onCharacter();
				}
				this.stringBuilder.append(ch);
			}
		}
		this.onRemainingText();		
	}
	
	public List<String> nextRow() throws IOException {
		List<String> values = new ArrayList<String>();
		while (true) {
			this.next();
			if (this.value != null) {
				values.add(this.value);
			}
			if (this.value == null || this.lineSeparator != null) {
				break;
			}
		}
		return Collections.unmodifiableList(values);
	}
	
	private void onCharacter() {
		if (this.isImmediatelyAfterEscaped) {
			String substring = this.stringBuilder.substring(
					this.escapedStartIndex + 1, this.escapedEndIndex);
			this.stringBuilder.replace(
					this.escapedStartIndex, this.escapedEndIndex + 1, substring);
			this.escapedStartIndex = -1;
			this.escapedEndIndex = -1;
			this.index -= 2;
			this.updateEscapeConditions();
		}
	}
	
	private void onDoubleQuoteCharacter() {
		if (this.isNonescaped) {
			this.escapedStartIndex = this.index;
			this.updateEscapeConditions();
		} else if (this.isEscaped) {
			this.escapedEndIndex = this.index;
			this.updateEscapeConditions();
		} else if (this.isImmediatelyAfterEscaped) {
			this.stringBuilder.replace(this.escapedEndIndex, this.index, "");
			this.escapedEndIndex = -1;
			this.index--;
			this.updateEscapeConditions();
		}
	}
	
	private void onNonescapedCarriageReturnCharacter() throws IOException {
		char ch = (char) this.chr;
		if ((this.chr = this.reader.read()) != -1) {
			char c = (char) this.chr;
			if (c == '\n') {
				this.lineSeparator = new String(new char[] { ch, c });
				this.removeEnclosedDoubleQuoteCharacters();						
				this.value = this.stringBuilder.toString();
				this.stringBuilder.delete(0, this.stringBuilder.length());
				this.stringBuilder = null;
				return;
			}
		}
		throw new CsvParserException(
				"field containing carriage return character ('\\r') "
				+ "must be escaped in double quotes or the carriage "
				+ "return character must be followed by a line feed "
				+ "character ('\\n')");		
	}
	
	private void onNonescapedCommaCharacter() {
		this.lineSeparator = null;
		this.removeEnclosedDoubleQuoteCharacters();
		this.value = this.stringBuilder.toString();
		this.stringBuilder.delete(0, this.stringBuilder.length());
	}
	
	private void onNonescapedLineFeedCharacter() throws IOException {
		throw new CsvParserException(
				"field containing line feed character ('\\n') must be "
				+ "escaped in double quotes or the line feed character "
				+ "must be preceeded by a carriage return character "
				+ "('\\r')");
	}
	
	private void onRemainingText() throws IOException {
		if (this.isEscaped) {
			throw new CsvParserException(
					"missing closing double quote character ('\"')");
		}
		this.lineSeparator = null;
		if (this.stringBuilder == null) {
			this.value = null;
		} else {
			if (this.isAfterEscaped) {
				String substring = this.stringBuilder.substring(
						this.escapedStartIndex + 1, this.escapedEndIndex);
				this.stringBuilder.replace(
						this.escapedStartIndex, this.escapedEndIndex + 1, substring);				
			}
			this.value = this.stringBuilder.toString();
			this.stringBuilder.delete(0, this.stringBuilder.length());
			this.stringBuilder = null;
		}
		this.escapedStartIndex = -1;
		this.escapedEndIndex = -1;
		this.index = -1;
		this.updateEscapeConditions();
	}
	
	private void removeEnclosedDoubleQuoteCharacters() {
		if (this.isImmediatelyAfterEscaped) {
			String substring = this.stringBuilder.substring(
					this.escapedStartIndex + 1, this.escapedEndIndex);
			this.stringBuilder.replace(
					this.escapedStartIndex, this.escapedEndIndex + 1, substring);
			this.escapedStartIndex = -1;
			this.escapedEndIndex = -1;
			this.index -= 2;
			this.updateEscapeConditions();
		}
	}
	
	private void updateEscapeConditions() {
		this.isAfterEscaped = this.escapedStartIndex > -1 
				&& this.escapedEndIndex > -1;
		this.isEscaped = this.escapedStartIndex > -1 
				&& this.escapedEndIndex == -1;
		this.isImmediatelyAfterEscaped = this.escapedStartIndex > -1 
				&& this.escapedEndIndex > -1
				&& this.escapedEndIndex == this.index - 1;
		this.isNonescaped = this.escapedStartIndex == -1;
		this.isNonescapedOrImmediatelyAfterEscaped = this.isNonescaped 
				|| this.isImmediatelyAfterEscaped;
	}

}
