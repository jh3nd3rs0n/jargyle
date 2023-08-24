package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class CsvParser {

	private String lineSeparator;
	private final Reader reader;
	private StringBuilder stringBuilder;	
	private String value;
	
	public CsvParser(final Reader rdr) {
		this.lineSeparator = null;
		this.reader = rdr;
		this.stringBuilder = null;		
		this.value = null;
	}
	
	public String getLineSeparator() {
		return this.lineSeparator;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void next() throws IOException {
		int chr = -1;
		while ((chr = this.reader.read()) != -1) {
			char ch = (char) chr;
			if (this.stringBuilder == null) {
				this.stringBuilder = new StringBuilder();
			}
			if (ch == ',') {
				this.lineSeparator = null;
				this.value = this.stringBuilder.toString();
				this.stringBuilder.delete(0, this.stringBuilder.length());
				return;
			} else if (ch == '\n') {
				if (this.stringBuilder.length() > 0) {
					char c = this.stringBuilder.charAt(
							this.stringBuilder.length() - 1);
					if (c == '\r') {
						this.lineSeparator = new String(
								new char[] { c, ch });
						this.value = this.stringBuilder.substring(
								0, this.stringBuilder.length() - 1);
					} else {
						this.lineSeparator = new String(new char[] { ch });
						this.value = this.stringBuilder.toString();
					}
				} else {
					this.lineSeparator = new String(new char[] { ch });
					this.value = this.stringBuilder.toString();
				}
				this.stringBuilder.delete(0, this.stringBuilder.length());
				this.stringBuilder = null;
				return;
			} else {
				this.stringBuilder.append(ch);
			}
		}
		this.lineSeparator = null;
		if (this.stringBuilder == null) {
			this.value = null;
		} else {
			this.value = this.stringBuilder.toString();
			this.stringBuilder.delete(0, this.stringBuilder.length());
			this.stringBuilder = null;
		}
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

}
