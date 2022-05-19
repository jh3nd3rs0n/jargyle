package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleResultSpecs {

	private final List<RuleResultSpec<Object>> ruleResultSpecs;
	private final Map<String, RuleResultSpec<Object>> ruleResultSpecsMap;
	
	public RuleResultSpecs() {
		this.ruleResultSpecs = new ArrayList<RuleResultSpec<Object>>();
		this.ruleResultSpecsMap = new HashMap<String, RuleResultSpec<Object>>();
	}
	
	public <T> RuleResultSpec<T> addThenGet(final RuleResultSpec<T> value) {
		@SuppressWarnings("unchecked")
		RuleResultSpec<Object> val = (RuleResultSpec<Object>) value;
		this.ruleResultSpecs.add(val);
		this.ruleResultSpecsMap.put(val.toString(), val);
		return value;
	}
	
	public List<RuleResultSpec<Object>> toList() {
		return Collections.unmodifiableList(this.ruleResultSpecs);
	}
	
	public Map<String, RuleResultSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.ruleResultSpecsMap);
	}

}
