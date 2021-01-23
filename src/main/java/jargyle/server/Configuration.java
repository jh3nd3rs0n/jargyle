package jargyle.server;

import java.util.Objects;

public abstract class Configuration {

	public static boolean equals(
			final Configuration config1, final Configuration config2) {
		if (config1 == null) {
			if (config2 != null) {
				return false;
			}
		} else {
			if (config2 == null) {
				return false;
			} else {
				if (!Objects.equals(config1.getSettings(), config2.getSettings())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Configuration() { }
	
	public abstract Settings getSettings();

}