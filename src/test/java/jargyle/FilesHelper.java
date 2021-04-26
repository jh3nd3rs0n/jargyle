package jargyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FilesHelper {
	
	public static void attemptsToDeleteIfExists(
			final Path path) throws IOException {
		int numOfRemainingAttempts = 3;
		boolean deleted = false;
		while (--numOfRemainingAttempts > 0 
				&& !(deleted = Files.deleteIfExists(path))) {
			ThreadHelper.sleepForThreeSeconds();
		}
		if (!deleted && path.toFile().exists()) {
			throw new IOException(String.format(
					"unable to delete existing '%s' despite 3 attempts", 
					path));
		}
	}
	
	private FilesHelper() { }
	
}
