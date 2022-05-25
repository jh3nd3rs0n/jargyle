package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SelectionStrategySettingSpec 
	extends SettingSpec<SelectionStrategy> {

	public SelectionStrategySettingSpec(
			final String s, final SelectionStrategy defaultVal) {
		super(s, SelectionStrategy.class, defaultVal);
	}

	@Override
	public Setting<SelectionStrategy> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(
				SelectionStrategy.valueOf(value).newMutableInstance());
	}

}
