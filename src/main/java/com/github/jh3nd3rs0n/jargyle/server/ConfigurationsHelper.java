package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public final class ConfigurationsHelper {
	
	public static boolean equals(final Configuration a, final Configuration b) {
		if (a == null) {
			if (b != null) {
				return false;
			}
		} else {
			if (b == null) {
				return false;
			} else {
				if (!Objects.equals(a.getSettings(), b.getSettings())) {
					return false;
				}
			}
		}
		return true;
	}

	private ConfigurationsHelper() { }
	
}
