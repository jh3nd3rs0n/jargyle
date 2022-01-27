package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public enum SelectionStrategy {
	
	@HelpText(
			doc = "Select the next in the cycle", 
			usage = "CYCLICAL"
	)
	CYCLICAL {
		@Override
		public <T> Selector<T> newSelector(final List<? extends T> list) {
			return Selector.newCyclicalSelector(list);
		}
	},

	@HelpText(
			doc = "Select the next at random", 
			usage = "RANDOM"
	)
	RANDOM {
		@Override
		public <T> Selector<T> newSelector(final List<? extends T> list) {
			return Selector.newRandomSelector(list);
		}
	};

	public static SelectionStrategy valueOfString(final String s) {
		SelectionStrategy selectionStrategy = null;
		try {
			selectionStrategy = SelectionStrategy.valueOf(s);
		} catch (IllegalArgumentException e) {
			String str = Arrays.stream(SelectionStrategy.values())
					.map(SelectionStrategy::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected selection strategy must be one of the "
					+ "following values: %s. actual value is %s",
					str,
					s));			
		}
		return selectionStrategy;		
	}
	
	public abstract <T> Selector<T> newSelector(final List<? extends T> list);
	
}
