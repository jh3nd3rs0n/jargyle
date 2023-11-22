package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SelectionStrategySettingSpec 
	extends SettingSpec<SelectionStrategy> {

	public SelectionStrategySettingSpec(
			final String n, final SelectionStrategy defaultVal) {
		super(n, SelectionStrategy.class, defaultVal);
	}

	@Override
	public Setting<SelectionStrategy> newSettingWithParsableValue(
			final String value) {
		return super.newSetting(SelectionStrategy.newInstance(value));
	}

}
