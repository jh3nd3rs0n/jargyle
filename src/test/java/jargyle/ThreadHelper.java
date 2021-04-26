package jargyle;

public final class ThreadHelper {

	private static final int THREE_SECONDS = 3000;
	
	public static void sleepForThreeSeconds() {
		try {
			Thread.sleep(THREE_SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private ThreadHelper() { }
	
}
