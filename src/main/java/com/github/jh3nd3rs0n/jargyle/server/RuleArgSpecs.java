package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleArgSpecs {

	private final List<RuleArgSpec<Object>> ruleArgSpecs;
	private final Map<String, RuleArgSpec<Object>> ruleArgSpecsMap;
	
	public RuleArgSpecs() {
		this.ruleArgSpecs = new ArrayList<RuleArgSpec<Object>>();
		this.ruleArgSpecsMap = new HashMap<String, RuleArgSpec<Object>>();
	}
	
	public <T> RuleArgSpec<T> addThenGet(final RuleArgSpec<T> value) {
		@SuppressWarnings("unchecked")
		RuleArgSpec<Object> val = (RuleArgSpec<Object>) value;
		this.ruleArgSpecs.add(val);
		this.ruleArgSpecsMap.put(val.toString(), val);
		return value;
	}
	
	public List<RuleArgSpec<Object>> toList() {
		return Collections.unmodifiableList(this.ruleArgSpecs);
	}
	
	public Map<String, RuleArgSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.ruleArgSpecsMap);
	}

}
