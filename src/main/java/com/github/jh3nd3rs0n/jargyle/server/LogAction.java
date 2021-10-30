package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LogAction {
	
	LOG_AS_WARNING {
		
		@Override
		public void log(final String message) {
			LOGGER.warn(message);
		}
		
	},
	
	LOG_AS_INFO {
		
		@Override
		public void log(final String message) {
			LOGGER.info(message);
		}
		
	};
	
	private static final Logger LOGGER = LoggerFactory.getLogger(
			LogAction.class);
	
	public static LogAction valueOfString(final String s) {
		LogAction logAction = null;
		try {
			logAction = LogAction.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<LogAction> list = Arrays.asList(LogAction.values());
			for (Iterator<LogAction> iterator = list.iterator();
					iterator.hasNext();) {
				LogAction value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(String.format(
					"expected log action must be one of the following values: "
					+ "%s. actual value is %s",
					sb.toString(),
					s));
		}
		return logAction;
	}
	
	public abstract void log(final String message);
	
}
