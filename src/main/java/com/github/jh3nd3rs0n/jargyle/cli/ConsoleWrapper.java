package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FilterInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

final class ConsoleWrapper {

	private static final class NullInputStream extends InputStream {
		
		@Override
		public int read() throws IOException {
			return -1;
		}
		
	}
	
	private static final class SwappableInputStream extends FilterInputStream {
		
		public SwappableInputStream(final InputStream i) {
			super(i);
		}
		
		public void setInputStream(final InputStream i) {
			this.in = i;
		}
		
	}
	
	private final Console console;
	
	public ConsoleWrapper(final Console cons) {
		this.console = cons;
	}
	
	public ConsoleWrapper printf(final String format, final Object... args) {
		if (this.console != null) {
			this.console.printf(format, args);
			return this;
		}
		System.out.printf(format, args);
		return this;
	}
	
	public String readLine(final String fmt, final Object... args) {
		if (this.console != null) {
			return this.console.readLine(fmt, args);
		}
		this.printf(fmt, args);
		String line = null;
		SwappableInputStream swapInputStream = new SwappableInputStream(
				System.in);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
	            swapInputStream));
		try {
			line = reader.readLine();
		} catch (IOException e) {
			throw new IOError(e);
		} finally {
			swapInputStream.setInputStream(new NullInputStream());
			try {
				reader.close();
			} catch (IOException e) {
				throw new IOError(e);
			}
		}
		return line;
	}
	
	public char[] readPassword(final String fmt, final Object... args) {
		if (this.console != null) {
			return this.console.readPassword(fmt, args);
		}
		return this.readLine(fmt, args).toCharArray();
	}
	
}
