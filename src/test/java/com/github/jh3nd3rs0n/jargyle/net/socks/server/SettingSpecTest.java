package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import org.junit.Test;

public class SettingSpecTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSettingSpecForInvalidPermissionObject() {
		new SettingSpec<Void>(new Object(), "void", Void.class, null) {

			@Override
			public Setting<Void> newSettingOfParsableValue(String value) {
				return this.newSetting(null);
			}
			
		};
	}

}
