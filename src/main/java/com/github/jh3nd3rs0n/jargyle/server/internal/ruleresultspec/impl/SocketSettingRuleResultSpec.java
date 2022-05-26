package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.server.RuleResult;
import com.github.jh3nd3rs0n.jargyle.server.RuleResultSpec;

public final class SocketSettingRuleResultSpec 
	extends RuleResultSpec<SocketSetting<Object>> {
	
	@SuppressWarnings("unchecked")
	private static Class<SocketSetting<Object>> valueType() {
		Class<SocketSetting<Object>> vType = null;
		try {
			vType = (Class<SocketSetting<Object>>) Class.forName(
					SocketSetting.class.getName());
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e);
		}
		return vType;
	}
	
	public SocketSettingRuleResultSpec(final String s) {
		super(s, valueType());
	}

	@Override
	public RuleResult<SocketSetting<Object>> newRuleResultOfParsableValue(
			final String value) {
		return super.newRuleResult(SocketSetting.newInstance(value));
	}

}
