package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class RuleConditionSpecs {

	private final List<RuleConditionSpec<Object, Object>> ruleConditionSpecs;
	private final Map<String, RuleConditionSpec<Object, Object>> ruleConditionSpecsMap;
	
	public RuleConditionSpecs() {
		this.ruleConditionSpecs = 
				new ArrayList<RuleConditionSpec<Object, Object>>();
		this.ruleConditionSpecsMap = 
				new HashMap<String, RuleConditionSpec<Object, Object>>();
	}
	
	public <T1, T2> RuleConditionSpec<T1, T2> addThenGet(
			final RuleConditionSpec<T1, T2> value) {
		@SuppressWarnings("unchecked")
		RuleConditionSpec<Object, Object> val = 
			(RuleConditionSpec<Object, Object>) value;
		this.ruleConditionSpecs.add(val);
		this.ruleConditionSpecsMap.put(val.getName(), val);
		return value;
	}
	
	public List<RuleConditionSpec<Object, Object>> toList() {
		return Collections.unmodifiableList(this.ruleConditionSpecs);
	}
	
	public Map<String, RuleConditionSpec<Object, Object>> toMap() {
		return Collections.unmodifiableMap(this.ruleConditionSpecsMap);
	}

}
