package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public enum LogAction {
	
	@HelpText(
			doc = "Log message as a warning message",
			usage = "LOG_AS_WARNING"
	)
	LOG_AS_WARNING {
		
		@Override
		public void invoke(final Logger logger, final String message) {
			logger.warn(message);
		}
		
	},
	
	@HelpText(
			doc = "Log message as an informational message",
			usage = "LOG_AS_INFO"
	)
	LOG_AS_INFO {
		
		@Override
		public void invoke(final Logger logger, final String message) {
			logger.info(message);
		}
		
	};
	
	public static LogAction valueOfString(final String s) {
		LogAction logAction = null;
		try {
			logAction = LogAction.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(LogAction.values())
					.map(LogAction::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected log action must be one of the following values: "
					+ "%s. actual value is %s",
					str,
					s));
		}
		return logAction;
	}
	
	public abstract void invoke(final Logger logger, final String message);
	
}
