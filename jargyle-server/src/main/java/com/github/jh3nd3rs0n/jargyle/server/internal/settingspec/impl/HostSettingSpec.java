package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class HostSettingSpec extends SettingSpec<Host> {

	public HostSettingSpec(final String n, final Host defaultVal) {
		super(n, Host.class, defaultVal);
	}

	@Override
	protected Host parse(final String value) {
		return Host.newInstance(value);
	}

}
