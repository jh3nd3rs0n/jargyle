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
		syntax = "[RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_ACTION1[,RULE_ACTION2[...]]",
		syntaxName = "RULE"
)
public final class Rule {

	public static final class Builder {
		
		private final List<RuleCondition<Object, Object>> ruleConditions;
		private final List<RuleAction<Object>> ruleActions;
		
		public Builder() {
			this.ruleConditions = new ArrayList<RuleCondition<Object, Object>>();
			this.ruleActions = new ArrayList<RuleAction<Object>>();
		}
		
		public Builder addRuleCondition(
				final RuleCondition<? extends Object, ? extends Object> ruleCondition) {
			@SuppressWarnings("unchecked")
			RuleCondition<Object, Object> rlCondition = 
					(RuleCondition<Object, Object>) ruleCondition;
			this.ruleConditions.add(rlCondition);
			return this;
		}
		
		public Builder addRuleAction(
				final RuleAction<? extends Object> ruleAction) {
			@SuppressWarnings("unchecked")
            RuleAction<Object> rlResult = (RuleAction<Object>) ruleAction;
			this.ruleActions.add(rlResult);
			return this;
		}
		
		public Rule build() {
			return new Rule(this);
		}
		
	}

	private static final Rule DEFAULT_INSTANCE = new Rule.Builder()
			.addRuleAction(GeneralRuleActionSpecConstants.FIREWALL_ACTION.newRuleAction(FirewallAction.ALLOW))
			.build();
	
	public static Rule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static Rule newInstanceFrom(final String s) {
		String[] entries = s.split(",", -1);
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
				builder.addRuleCondition(RuleCondition.newInstanceFrom(entry));
				continue;
			}
			ex = null;
			try {
				RuleActionSpecConstants.valueOfName(name);
			} catch (IllegalArgumentException e) {
				ex = e;
			}
			if (ex == null) {
				builder.addRuleAction(RuleAction.newInstanceFrom(entry));
				continue;
			}
			throw new IllegalArgumentException(String.format(
					"unknown rule condition or rule action: %s", 
					name));			
		}
		return builder.build();
	}
	
	private final Map<RuleConditionSpec<Object, Object>, List<RuleCondition<Object, Object>>> ruleConditionListMap;
	private final List<RuleCondition<Object, Object>> ruleConditions;
	private final Map<RuleActionSpec<Object>, List<RuleAction<Object>>> ruleActionListMap;
	private final List<RuleAction<Object>> ruleActions;
	
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
		List<RuleAction<Object>> rlResults = 
				new ArrayList<RuleAction<Object>>(builder.ruleActions);
		Map<RuleActionSpec<Object>, List<RuleAction<Object>>> rlResultListMap = 
				new LinkedHashMap<RuleActionSpec<Object>, List<RuleAction<Object>>>();
		for (RuleAction<Object> rlResult : rlResults) {
			RuleActionSpec<Object> rlResultSpec = rlResult.getRuleActionSpec();
			List<RuleAction<Object>> results = rlResultListMap.get(rlResultSpec);
			if (results == null) {
				results = new ArrayList<RuleAction<Object>>();
				rlResultListMap.put(rlResultSpec, results);
			}
			results.add(rlResult);
		}
		this.ruleConditionListMap = rlConditionListMap;
		this.ruleConditions = rlConditions;
		this.ruleActionListMap = rlResultListMap;
		this.ruleActions = rlResults;
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
		if (this.ruleActions == null) {
			if (other.ruleActions != null) {
				return false;
			}
		} else if (!this.ruleActions.equals(other.ruleActions)) {
			return false;
		}
		return true;
	}
	
	public <V> V getLastRuleActionValue(final RuleActionSpec<V> ruleActionSpec) {
		List<RuleAction<Object>> ruleActionList = this.ruleActionListMap.get(
				ruleActionSpec);
		V value = null;
		if (ruleActionList != null && ruleActionList.size() > 0) {
			RuleAction<Object> ruleAction = ruleActionList.get(
					ruleActionList.size() - 1);
			value = ruleActionSpec.getValueType().cast(ruleAction.getValue());
		}
		return value;
	}

	public List<RuleCondition<Object, Object>> getRuleConditions() {
		return Collections.unmodifiableList(this.ruleConditions);
	}
	
	public List<RuleAction<Object>> getRuleActions() {
		return Collections.unmodifiableList(this.ruleActions);
	}
	
	public <V> List<V> getRuleActionValues(final RuleActionSpec<V> ruleActionSpec) {
		List<RuleAction<Object>> ruleActionList = this.ruleActionListMap.get(
				ruleActionSpec);
		List<V> values = new ArrayList<V>();
		if (ruleActionList != null) {
			for (RuleAction<Object> ruleAction : ruleActionList) {
				values.add(ruleActionSpec.getValueType().cast(
						ruleAction.getValue()));
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
		result = prime * result + ((this.ruleActions == null) ? 
				0 : this.ruleActions.hashCode());
		return result;
	}
	
	public boolean hasRuleCondition(
			final RuleConditionSpec<? extends Object, ? extends Object> ruleConditionSpec) {
		return this.ruleConditionListMap.containsKey(ruleConditionSpec);
	}
	
	public boolean hasRuleAction(
			final RuleActionSpec<? extends Object> ruleActionSpec) {
		return this.ruleActionListMap.containsKey(ruleActionSpec);
	}

	@Override
	public String toString() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(this.ruleConditions);
		list.addAll(this.ruleActions);
		return list.stream()
				.map(Object::toString)
				.collect(Collectors.joining(","));
	}
	
}
