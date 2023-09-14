package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class RuleContext {

	private final Map<RuleArgSpec<Object>, RuleArg<Object>> ruleArgMap;
	
	public RuleContext() {
		this.ruleArgMap = new LinkedHashMap<RuleArgSpec<Object>, RuleArg<Object>>();
	}
	
	public RuleContext(final RuleContext other) {
		RuleContext oth = other;
		if (oth == null) {
			oth = new RuleContext();
		}
		this.ruleArgMap = new LinkedHashMap<RuleArgSpec<Object>, RuleArg<Object>>(
				oth.ruleArgMap);
	}
	
	public <V> boolean containsRuleArgSpecKey(
			final RuleArgSpec<V> ruleArgSpec) {
		return this.ruleArgMap.containsKey(ruleArgSpec);
	}
	
	public Map<RuleArgSpec<Object>, RuleArg<Object>> getRuleArgMap() {
		return Collections.unmodifiableMap(this.ruleArgMap);
	}
	
	public <V> V getRuleArgValue(final RuleArgSpec<V> ruleArgSpec) {
		V value = null;
		RuleArg<Object> ruleArg = this.ruleArgMap.get(ruleArgSpec);
		if (ruleArg != null) {
			value = ruleArgSpec.getValueType().cast(ruleArg.getValue());
		}
		return value;
	}
	
	public <V> V putRuleArgValue(final RuleArgSpec<V> ruleArgSpec, final V value) {
		@SuppressWarnings("unchecked")
		RuleArgSpec<Object> rlArgSpec = (RuleArgSpec<Object>) ruleArgSpec; 
		RuleArg<Object> rlArg = rlArgSpec.newRuleArg(
				ruleArgSpec.getValueType().cast(value));
		V recentValue = this.removeRuleArgValue(ruleArgSpec);
		this.ruleArgMap.put(rlArgSpec, rlArg);
		return recentValue;
	}
	
	public <V> V removeRuleArgValue(final RuleArgSpec<V> ruleArgSpec) {
		V recentValue = null;
		@SuppressWarnings("unchecked")
		RuleArgSpec<Object> rlArgSpec = (RuleArgSpec<Object>) ruleArgSpec;
		RuleArg<Object> recentRlArg = this.ruleArgMap.remove(rlArgSpec);
		if (recentRlArg != null) {
			recentValue = ruleArgSpec.getValueType().cast(
					recentRlArg.getValue());
		}
		return recentValue;
	}
	
	@Override
	public String toString() {
		return this.ruleArgMap.values().stream()
				.map(Object::toString)
				.collect(Collectors.joining(" "));
	}
	
}
