package jargyle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

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
		try (InputStream in = classLoader.getResourceAsStream(name)) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b = -1;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			string = new String(out.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return string;
	}
	
	private ResourceHelper() { }
	
}
