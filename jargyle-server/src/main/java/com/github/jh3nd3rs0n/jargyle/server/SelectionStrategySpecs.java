package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SelectionStrategySpecs {

	private final List<SelectionStrategySpec> selectionStrategySpecs;
	private final Map<String, SelectionStrategySpec> selectionStrategySpecsMap;
	
	public SelectionStrategySpecs() {
		this.selectionStrategySpecs = new ArrayList<SelectionStrategySpec>();
		this.selectionStrategySpecsMap = 
				new HashMap<String, SelectionStrategySpec>();
	}
	
	public SelectionStrategySpec addThenGet(final SelectionStrategySpec value) {
		this.selectionStrategySpecs.add(value);
		this.selectionStrategySpecsMap.put(value.getName(), value);
		return value;
	}
	
	public List<SelectionStrategySpec> toList() {
		return Collections.unmodifiableList(this.selectionStrategySpecs);
	}
	
	public Map<String, SelectionStrategySpec> toMap() {
		return Collections.unmodifiableMap(this.selectionStrategySpecsMap);
	}
	
}
