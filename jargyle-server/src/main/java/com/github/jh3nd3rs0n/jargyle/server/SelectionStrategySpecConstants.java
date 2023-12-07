package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl.CyclicalSelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl.RandomSelectionStrategy;

@SingleValueSpecsDoc(
		description = "",
		name = "Selection Strategies"
)
public final class SelectionStrategySpecConstants {
	
	private static final SelectionStrategySpecs SELECTION_STRATEGY_SPECS =
			new SelectionStrategySpecs();
	
	@SingleValueSpecDoc(
			description = "Select the next in the cycle", 
			name = "CYCLICAL",
			syntax = "CYCLICAL"
	)	
	public static final SelectionStrategySpec CYCLICAL = SELECTION_STRATEGY_SPECS.addThenGet(new SelectionStrategySpec(
			"CYCLICAL") {

				@Override
				public SelectionStrategy newSelectionStrategy() {
					return new CyclicalSelectionStrategy(this);
				}
		
	}); 

	@SingleValueSpecDoc(
			description = "Select the next at random", 
			name = "RANDOM",
			syntax = "RANDOM"
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
				"expected selection strategy name must be one of the "
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
