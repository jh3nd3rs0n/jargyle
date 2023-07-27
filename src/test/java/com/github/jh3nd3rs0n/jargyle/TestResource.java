package com.github.jh3nd3rs0n.jargyle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public final class TestResource {

	private final String name;
	
	TestResource(final String nm) {
		this.name = nm;
	}
	
	public String getContentAsString() {
		ClassLoader classLoader = TestResource.class.getClassLoader();
		InputStream in = classLoader.getResourceAsStream(this.name);
		String string = null;
		try (Reader reader = new InputStreamReader(in, Charset.forName("UTF-8"))) {
			string = IoHelper.readStringFrom(reader);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return string;
	}
	
	public File getFile() {
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
	
}
