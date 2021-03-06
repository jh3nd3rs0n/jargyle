package jargyle;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public final class ResourceHelper {
	
	public static File getResourceAsFile(final String name) {
		ClassLoader classLoader = ResourceHelper.class.getClassLoader();
		URL url = classLoader.getResource(name);
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
		return file;
	}
	
	public static String getResourceAsString(final String name) {
		ClassLoader classLoader = ResourceHelper.class.getClassLoader();
		String string = null;
		try (Reader reader = new InputStreamReader(
				classLoader.getResourceAsStream(name), 
				Charset.forName("UTF-8"))) {
			string = IoHelper.readStringFrom(reader);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return string;
	}
	
	private ResourceHelper() { }
	
}
