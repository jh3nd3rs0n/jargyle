package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public enum LogAction {
	
	@HelpText(
			doc = "Log at the WARNING level",
			usage = "LOG_AS_WARNING"
	)
	LOG_AS_WARNING {
		
		@Override
		public void invoke(final String message) {
			LOGGER.warn(message);
		}
		
	},
	
	@HelpText(
			doc = "Log at the INFO level",
			usage = "LOG_AS_INFO"
	)
	LOG_AS_INFO {
		
		@Override
		public void invoke(final String message) {
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
	
	public abstract void invoke(final String message);
	
}
