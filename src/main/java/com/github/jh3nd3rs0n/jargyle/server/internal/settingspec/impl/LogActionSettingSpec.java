package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class LogActionSettingSpec extends SettingSpec<LogAction> {

	public LogActionSettingSpec(final String s,	final LogAction defaultVal) {
		super(s, LogAction.class, defaultVal);
	}

	@Override
	public Setting<LogAction> newSettingOfParsableValue(final String value) {
		return super.newSetting(LogAction.valueOfString(value));
	}

}
