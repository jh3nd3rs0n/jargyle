package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleActionSpecs {

	private final List<RuleActionSpec<Object>> ruleActionSpecs;
	private final Map<String, RuleActionSpec<Object>> ruleActionSpecsMap;
	
	public RuleActionSpecs() {
		this.ruleActionSpecs = new ArrayList<RuleActionSpec<Object>>();
		this.ruleActionSpecsMap = new HashMap<String, RuleActionSpec<Object>>();
	}
	
	public <T> RuleActionSpec<T> addThenGet(final RuleActionSpec<T> value) {
		@SuppressWarnings("unchecked")
        RuleActionSpec<Object> val = (RuleActionSpec<Object>) value;
		this.ruleActionSpecs.add(val);
		this.ruleActionSpecsMap.put(val.getName(), val);
		return value;
	}
	
	public List<RuleActionSpec<Object>> toList() {
		return Collections.unmodifiableList(this.ruleActionSpecs);
	}
	
	public Map<String, RuleActionSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.ruleActionSpecsMap);
	}

}
