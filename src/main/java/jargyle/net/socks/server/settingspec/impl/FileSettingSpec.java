package jargyle.net.socks.server.settingspec.impl;

import java.io.File;

import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

final class FileSettingSpec extends SettingSpec<File> {

	public FileSettingSpec(final String s, final File defaultVal) {
		super(s, File.class, defaultVal);
	}

	@Override
	public Setting<File> newSetting(final File value) {
		if (!value.exists()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' does not exist", 
					value));
		}
		if (!value.isFile()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' must be a file", 
					value));
		}
		return super.newSetting(value);
	}

	@Override
	public Setting<File> newSettingOfParsableValue(final String value) {
		return newSetting(new File(value));
	}

}
