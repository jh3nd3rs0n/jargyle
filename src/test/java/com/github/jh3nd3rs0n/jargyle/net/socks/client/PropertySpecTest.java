package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import org.junit.Test;

public class PropertySpecTest {

	@Test(expected = IllegalArgumentException.class)
	public void testPropertySpecForInvalidPermissionObject() {
		new PropertySpec<Void>(new Object(), "void", Void.class, null) {

			@Override
			public Property<Void> newPropertyOfParsableValue(String value) {
				return this.newProperty(null);
			}
			
		};
	}

}
