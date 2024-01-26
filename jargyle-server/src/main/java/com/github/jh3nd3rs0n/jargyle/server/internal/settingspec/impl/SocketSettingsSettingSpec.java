package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SocketSettingsSettingSpec 
	extends SettingSpec<SocketSettings> {

	public SocketSettingsSettingSpec(
			final String n, final SocketSettings defaultVal) {
		super(n, SocketSettings.class, defaultVal);
	}

	@Override
	protected SocketSettings parse(final String value) {
		return SocketSettings.newInstanceFrom(value);
	}

}
