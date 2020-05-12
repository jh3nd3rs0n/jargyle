package jargyle.server;

import java.util.logging.Logger;

final class LoggerHolder {

	public static final Logger LOGGER = Logger.getLogger(
			LoggerHolder.class.getPackage().getName());
	
	private LoggerHolder() { }
}
