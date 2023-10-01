package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl.CyclicalSelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl.RandomSelectionStrategy;

public final class SelectionStrategySpecConstants {
	
	private static final SelectionStrategySpecs SELECTION_STRATEGY_SPECS =
			new SelectionStrategySpecs();
	
	@HelpText(
			doc = "Select the next in the cycle", 
			usage = "CYCLICAL"
	)	
	public static final SelectionStrategySpec CYCLICAL = SELECTION_STRATEGY_SPECS.addThenGet(new SelectionStrategySpec(
			"CYCLICAL") {

				@Override
				public SelectionStrategy newSelectionStrategy() {
					return new CyclicalSelectionStrategy(this);
				}
		
	}); 

	@HelpText(
			doc = "Select the next at random", 
			usage = "RANDOM"
	)
	public static final SelectionStrategySpec RANDOM = SELECTION_STRATEGY_SPECS.addThenGet(new SelectionStrategySpec(
			"RANDOM") {

				@Override
				public SelectionStrategy newSelectionStrategy() {
					return new RandomSelectionStrategy(this);
				}
		
	});

	private static final List<SelectionStrategySpec> VALUES = 
			SELECTION_STRATEGY_SPECS.toList();
	
	private static final Map<String, SelectionStrategySpec> VALUES_MAP =
			SELECTION_STRATEGY_SPECS.toMap();
	
	public static SelectionStrategySpec valueOfName(final String name) {
		if (VALUES_MAP.containsKey(name)) {
			return VALUES_MAP.get(name);
		}
		String str = VALUES.stream()
				.map(SelectionStrategySpec::getName)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected selection strategy spec must be one of the "
				+ "following values: %s. actual value is %s",
				str,
				name));
	}
	
	public static List<SelectionStrategySpec> values() {
		return VALUES;
	}
	
	public static Map<String, SelectionStrategySpec> valuesMap() {
		return VALUES_MAP;
	}
	
	private SelectionStrategySpecConstants() { }
	
}
