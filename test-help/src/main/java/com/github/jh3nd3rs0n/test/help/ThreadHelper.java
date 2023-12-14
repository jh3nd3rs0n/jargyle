package com.github.jh3nd3rs0n.test.help;

/**
 * Helper class for thread operations for testing.
 */
public final class ThreadHelper {

	/** 3 seconds in milliseconds. */
	private static final int THREE_SECONDS = 3000;

	/**
	 * Causes the currently executing thread to sleep for 3 seconds.
	 */
	public static void sleepForThreeSeconds() {
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Prevents the construction of unnecessary instances.
	 */
	private ThreadHelper() { }
	
}
