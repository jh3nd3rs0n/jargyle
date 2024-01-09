package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

@SingleValueTypeDoc(
		description = "",
		name = "Rule",
		syntax = "[RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_RESULT1[,RULE_RESULT2[...]]",
		syntaxName = "RULE"
)
public final class Rule {

	public static final class Builder {
		
		private final List<RuleCondition<Object, Object>> ruleConditions;
		private final List<RuleResult<Object>> ruleResults;
		
		public Builder() {
			this.ruleConditions = new ArrayList<RuleCondition<Object, Object>>();
			this.ruleResults = new ArrayList<RuleResult<Object>>();
		}
		
		public Builder addRuleCondition(
				final RuleCondition<? extends Object, ? extends Object> ruleCondition) {
			@SuppressWarnings("unchecked")
			RuleCondition<Object, Object> rlCondition = 
					(RuleCondition<Object, Object>) ruleCondition;
			this.ruleConditions.add(rlCondition);
			return this;
		}
		
		public Builder addRuleResult(
				final RuleResult<? extends Object> ruleResult) {
			@SuppressWarnings("unchecked")
			RuleResult<Object> rlResult = (RuleResult<Object>) ruleResult;
			this.ruleResults.add(rlResult);
			return this;
		}
		
		public Rule build() {
			return new Rule(this);
		}
		
	}

	private static final Rule DEFAULT_INSTANCE = new Rule.Builder()
			.addRuleResult(GeneralRuleResultSpecConstants.FIREWALL_ACTION.newRuleResult(FirewallAction.ALLOW))
			.build();
	
	public static Rule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Rule newInstanceOf(final String s) {
		String[] entries = s.split(",");
		Builder builder = new Builder();
		for (String entry : entries) {
			String[] entryElements = entry.split("=", 2);
			String name = entryElements[0];
			IllegalArgumentException ex = null;
			try {
				RuleConditionSpecConstants.valueOfName(name);
			} catch (IllegalArgumentException e) {
				ex = e;
			}
			if (ex == null) {
				builder.addRuleCondition(RuleCondition.newInstanceOf(entry));
				continue;
			}
			ex = null;
			try {
				RuleResultSpecConstants.valueOfName(name);
			} catch (IllegalArgumentException e) {
				ex = e;
			}
			if (ex == null) {
				builder.addRuleResult(RuleResult.newInstanceOf(entry));
				continue;
			}
			throw new IllegalArgumentException(String.format(
					"unknown rule condition or rule result: %s", 
					name));			
		}
		return builder.build();
	}
	
	private final Map<RuleConditionSpec<Object, Object>, List<RuleCondition<Object, Object>>> ruleConditionListMap;
	private final List<RuleCondition<Object, Object>> ruleConditions;
	private final Map<RuleResultSpec<Object>, List<RuleResult<Object>>> ruleResultListMap;
	private final List<RuleResult<Object>> ruleResults;
	
	private Rule(final Builder builder) {
		List<RuleCondition<Object, Object>> rlConditions = 
				new ArrayList<RuleCondition<Object, Object>>(
						builder.ruleConditions);
		Map<RuleConditionSpec<Object, Object>, List<RuleCondition<Object, Object>>> rlConditionListMap =
				new LinkedHashMap<RuleConditionSpec<Object, Object>, List<RuleCondition<Object, Object>>>();
		for (RuleCondition<Object, Object> rlCondition : rlConditions) {
			RuleConditionSpec<Object, Object> rlConditionSpec = 
					rlCondition.getRuleConditionSpec();
			List<RuleCondition<Object, Object>> conditions = 
					rlConditionListMap.get(rlConditionSpec);
			if (conditions == null) {
				conditions = new ArrayList<RuleCondition<Object, Object>>();
				rlConditionListMap.put(rlConditionSpec, conditions);
			}
			conditions.add(rlCondition);
		}
		List<RuleResult<Object>> rlResults = 
				new ArrayList<RuleResult<Object>>(builder.ruleResults);
		Map<RuleResultSpec<Object>, List<RuleResult<Object>>> rlResultListMap = 
				new LinkedHashMap<RuleResultSpec<Object>, List<RuleResult<Object>>>();
		for (RuleResult<Object> rlResult : rlResults) {
			RuleResultSpec<Object> rlResultSpec = rlResult.getRuleResultSpec();
			List<RuleResult<Object>> results = rlResultListMap.get(rlResultSpec);
			if (results == null) {
				results = new ArrayList<RuleResult<Object>>();
				rlResultListMap.put(rlResultSpec, results);
			}
			results.add(rlResult);
		}
		this.ruleConditionListMap = rlConditionListMap;
		this.ruleConditions = rlConditions;
		this.ruleResultListMap = rlResultListMap;
		this.ruleResults = rlResults;
	}
	
	public boolean appliesTo(final RuleContext context) {
		for (List<RuleCondition<Object, Object>> ruleConditionList 
				: this.ruleConditionListMap.values()) {
			boolean anyRuleConditionEvaluatesTrue = false;
			for (RuleCondition<Object, Object> ruleCondition : ruleConditionList) {
				if (ruleCondition.evaluatesTrue(context)) {
					anyRuleConditionEvaluatesTrue = true;
					break;
				}
			}
			if (!anyRuleConditionEvaluatesTrue) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Rule other = (Rule) obj;
		if (this.ruleConditions == null) {
			if (other.ruleConditions != null) {
				return false;
			}
		} else if (!this.ruleConditions.equals(other.ruleConditions)) {
			return false;
		}
		if (this.ruleResults == null) {
			if (other.ruleResults != null) {
				return false;
			}
		} else if (!this.ruleResults.equals(other.ruleResults)) {
			return false;
		}
		return true;
	}
	
	public <V> V getLastRuleResultValue(final RuleResultSpec<V> ruleResultSpec) {
		List<RuleResult<Object>> ruleResultList = this.ruleResultListMap.get(
				ruleResultSpec);
		V value = null;
		if (ruleResultList != null && ruleResultList.size() > 0) {
			RuleResult<Object> ruleResult = ruleResultList.get(
					ruleResultList.size() - 1);
			value = ruleResultSpec.getValueType().cast(ruleResult.getValue());
		}
		return value;
	}

	public List<RuleCondition<Object, Object>> getRuleConditions() {
		return Collections.unmodifiableList(this.ruleConditions);
	}
	
	public List<RuleResult<Object>> getRuleResults() {
		return Collections.unmodifiableList(this.ruleResults);
	}
	
	public <V> List<V> getRuleResultValues(final RuleResultSpec<V> ruleResultSpec) {
		List<RuleResult<Object>> ruleResultList = this.ruleResultListMap.get(
				ruleResultSpec);
		List<V> values = new ArrayList<V>();
		if (ruleResultList != null) {
			for (RuleResult<Object> ruleResult : ruleResultList) {
				values.add(ruleResultSpec.getValueType().cast(
						ruleResult.getValue()));
			}
		}
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ruleConditions == null) ? 
				0 : this.ruleConditions.hashCode());
		result = prime * result + ((this.ruleResults == null) ? 
				0 : this.ruleResults.hashCode());
		return result;
	}
	
	public boolean hasRuleCondition(
			final RuleConditionSpec<? extends Object, ? extends Object> ruleConditionSpec) {
		return this.ruleConditionListMap.containsKey(ruleConditionSpec);
	}
	
	public boolean hasRuleResult(
			final RuleResultSpec<? extends Object> ruleResultSpec) {
		return this.ruleResultListMap.containsKey(ruleResultSpec);
	}

	@Override
	public String toString() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.ruleConditions);
		list.addAll(this.ruleResults);
		return list.stream()
				.map(Object::toString)
				.collect(Collectors.joining(","));
	}
	
}
