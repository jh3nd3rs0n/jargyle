package jargyle.server.socks5;

final class ThrowableHelper {

	public static String toString(final Throwable t) {
		StringBuilder sb = new StringBuilder();
		sb.append(t.getClass().getName());
		String message = t.getMessage();
		if (message != null) {
			sb.append(": ");
			sb.append(message);
			return sb.toString();
		}
		Throwable cause = t.getCause();
		if (cause != null) {
			sb.append(": ");
			sb.append(cause);
			return sb.toString();
		}
		return sb.toString();
	}
	
	private ThrowableHelper() { }
	
}
