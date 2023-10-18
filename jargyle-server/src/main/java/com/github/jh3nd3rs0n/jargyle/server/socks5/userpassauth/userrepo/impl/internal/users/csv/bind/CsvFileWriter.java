package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.internal.users.csv.bind;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

final class CsvFileWriter {

	private final String lineSeparator;
	private final Writer writer;
	
	public CsvFileWriter(final Writer wrtr) {
		this.lineSeparator = System.getProperty("line.separator");
		this.writer = wrtr;
	}
	
	public void writeRecord(final List<String> fields) throws IOException {
		for (Iterator<String> iterator = fields.iterator(); 
				iterator.hasNext();) {
			String field = iterator.next();
			if (field.indexOf('\"') > -1) {
				field = field.replace("\"", "\"\"");
			}
			if (field.indexOf('\"') > -1 
					|| field.indexOf(',') > -1
					|| field.indexOf('\r') > -1
					|| field.indexOf('\n') > -1) {
				field = String.format("\"%s\"", field);
			}
			this.writer.write(field);
			if (iterator.hasNext()) {
				this.writer.write(",");
			} else {
				this.writer.write(this.lineSeparator);
			}
		}
		if (fields.size() > 0) {
			this.writer.flush();
		}
	}
	
	public void writeRecord(final String... fields) throws IOException {
		this.writeRecord(Arrays.asList(fields));
	}

}
