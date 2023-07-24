package com.github.jh3nd3rs0n.jargyle.clientserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public final class TestResource {

	private final String name;
	
	TestResource(final String nm) {
		this.name = nm;
	}
	
	public File asFile() {
		ClassLoader classLoader = TestResource.class.getClassLoader();
		URL url = classLoader.getResource(this.name);
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
		return file;
	}
	
	public String asString() {
		ClassLoader classLoader = TestResource.class.getClassLoader();
		InputStream in = classLoader.getResourceAsStream(this.name);
		String string = null;
		try (Reader reader = new InputStreamReader(in, Charset.forName("UTF-8")); 
				StringWriter writer = new StringWriter()) {
			int ch = -1;
			while ((ch = reader.read()) != -1) {
				writer.write(ch);
			}
			writer.flush();
			string = writer.toString();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return string;
	}
	
}
