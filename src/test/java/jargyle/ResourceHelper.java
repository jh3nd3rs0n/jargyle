package jargyle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public final class ResourceHelper {

	private static final String SERVER_KEY_STORE_FILE = 
			"jargyle/server/server.jks";
	private static final String SERVER_KEY_STORE_PASSWORD_FILE = 
			"jargyle/server/server.jks.password";
	
	public static File getServerKeyStoreFile() {
		ClassLoader classLoader = ResourceHelper.class.getClassLoader();
		URL keyStoreUrl = classLoader.getResource(SERVER_KEY_STORE_FILE);
		File keyStoreFile = null;
		try {
			keyStoreFile = new File(keyStoreUrl.toURI());
		} catch (URISyntaxException e) {
			throw new AssertionError(e);
		}
		return keyStoreFile;
	}
	
	public static String getServerKeyStorePassword() {
		ClassLoader classLoader = ResourceHelper.class.getClassLoader();
		String keyStorePassword = null;
		try (InputStream keyStorePasswordIn = classLoader.getResourceAsStream(
				SERVER_KEY_STORE_PASSWORD_FILE)) {
			ByteArrayOutputStream keyStorePasswordOut = 
					new ByteArrayOutputStream();
			int b = -1;
			while ((b = keyStorePasswordIn.read()) != -1) {
				keyStorePasswordOut.write(b);
			}
			keyStorePassword = new String(keyStorePasswordOut.toByteArray());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		return keyStorePassword;
	}
	
	private ResourceHelper() { }
	
}
