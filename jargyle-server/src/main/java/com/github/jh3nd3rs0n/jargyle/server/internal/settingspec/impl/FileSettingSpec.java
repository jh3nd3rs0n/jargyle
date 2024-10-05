package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

import java.io.File;

public final class FileSettingSpec extends SettingSpec<File> {

	public FileSettingSpec(final String n, final File defaultVal) {
		super(n, File.class, defaultVal);
	}

	@Override
	protected File parse(final String value) {
		return new File(value);
	}

	@Override
	protected File validate(final File value) {
		if (!value.exists()) {
			throw new IllegalArgumentException(String.format(
					"`%s' does not exist",
					value));
		}
		if (!value.isFile()) {
			throw new IllegalArgumentException(String.format(
					"`%s' must be a file",
					value));
		}
		return value;
	}

}
