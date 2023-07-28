package com.github.jh3nd3rs0n.jargyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FilesHelper {
	
	private static final int MAX_NUM_OF_REMAINING_ATTEMPTS = 3;
	
	public static void attemptsToDeleteIfExists(
			final Path path) throws IOException {
		/*
		int numOfRemainingAttempts = MAX_NUM_OF_REMAINING_ATTEMPTS;
		while (path.toFile().exists() 
				&& --numOfRemainingAttempts > 0 
				&& !Files.deleteIfExists(path)) {
			// ThreadHelper.sleepForThreeSeconds();
		}
		if (path.toFile().exists() && numOfRemainingAttempts == 0) {
			throw new IOException(String.format(
					"unable to delete existing '%s' despite %s attempts", 
					path,
					MAX_NUM_OF_REMAINING_ATTEMPTS));
		}
		*/
		Files.deleteIfExists(path);
	}
	
	private FilesHelper() { }
	
}
