package com.github.jh3nd3rs0n.jargyle.server.internal.ruleresultspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
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
	
	public SocketSettingRuleResultSpec(final String n) {
		super(n, valueType());
	}

	@Override
	protected SocketSetting<Object> parse(final String value) {
		return SocketSetting.newInstanceFrom(value);
	}

}
