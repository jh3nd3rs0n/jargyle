package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Words;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class WordsSettingSpec extends SettingSpec<Words> {

	public WordsSettingSpec(
			final Object permission, 
			final String s, 
			final Words defaultVal) {
		super(permission, s, Words.class, defaultVal);
	}

	@Override
	public Setting<Words> newSettingOfParsableValue(final String value) {
		return super.newSetting(Words.newInstance(value));
	}

}
