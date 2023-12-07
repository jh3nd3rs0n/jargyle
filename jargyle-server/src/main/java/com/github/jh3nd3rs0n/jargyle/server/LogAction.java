package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

@EnumValueTypeDoc(
		description = "",
		name = "Log Action",
		syntax = "LOG_AS_WARNING|LOG_AS_INFO",
		syntaxName = "LOG_ACTION"
)
public enum LogAction {
	
	@EnumValueDoc(
			description = "Log message as a warning message",
			value = "LOG_AS_WARNING"
	)
	LOG_AS_WARNING {
		
		@Override
		public void invoke(final String message) {
			LOGGER.warn(message);
		}
		
	},
	
	@EnumValueDoc(
			description = "Log message as an informational message",
			value = "LOG_AS_INFO"
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
	
	public abstract void invoke(final String message);
	
}
