package com.github.jh3nd3rs0n.test.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class IoHelper {

	public static String readStringFrom(final File file) throws IOException {
		String string = null;
		try (Reader reader = new InputStreamReader(
				new FileInputStream(file), Charset.forName("UTF-8"))) {
			string = readStringFrom(reader);
		}
		return string;
	}
	
	public static String readStringFrom(
			final Reader reader) throws IOException {
		String string = null;
		try (StringWriter writer = new StringWriter()) {
			int ch = -1;
			while ((ch = reader.read()) != -1) {
				writer.write(ch);
			}
			writer.flush();
			string = writer.toString();
		}
		return string;
	}
	
	public static void writeStringThenFlush(
			final String string, final Writer writer) throws IOException {
		writer.write(string);
		writer.flush();
	}
	
	public static void writeStringToFile(
			final String string, final File file) throws IOException {
		File tempFile = new File(file.toString().concat(".tmp"));
		try (Writer writer = new OutputStreamWriter(
				new FileOutputStream(tempFile), Charset.forName("UTF-8"))) {
			writeStringThenFlush(string, writer);
		}
		Files.move(
				tempFile.toPath(), 
				file.toPath(),
				StandardCopyOption.ATOMIC_MOVE, 
				StandardCopyOption.REPLACE_EXISTING);
	}
	
	private IoHelper() { }
	
}
