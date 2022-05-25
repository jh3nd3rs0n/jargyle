package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.text.Words;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class WordsSettingSpec extends SettingSpec<Words> {

	public WordsSettingSpec(final String s,	final Words defaultVal) {
		super(s, Words.class, defaultVal);
	}

	@Override
	public Setting<Words> newSettingOfParsableValue(final String value) {
		return super.newSetting(Words.newInstance(value));
	}

}
