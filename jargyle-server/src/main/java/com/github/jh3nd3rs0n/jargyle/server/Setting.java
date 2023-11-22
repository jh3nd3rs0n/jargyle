package com.github.jh3nd3rs0n.jargyle.server;

public final class Setting<V> {
	
	public static Setting<Object> newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"setting must be in the following format: NAME=VALUE");
		}
		String name = sElements[0];
		String value = sElements[1];
		return newInstanceWithParsableValue(name, value);
	}
	
	public static <V> Setting<V> newInstance(final String name, final V value) {
		SettingSpec<Object> settingSpec = SettingSpecConstants.valueOfName(
				name);
		@SuppressWarnings("unchecked")
		Setting<V> setting = (Setting<V>) settingSpec.newSetting(value);
		return setting;
	}

	public static <V> Setting<V> newInstance(
			final String name, final V value, final String doc) {
		Setting<V> setting = newInstance(name, value);
		return new Setting<V>(
				setting.getSettingSpec(), setting.getValue(), doc);
	}
	
	public static Setting<Object> newInstanceWithParsableValue(
			final String name, final String value) {
		SettingSpec<Object> settingSpec = SettingSpecConstants.valueOfName(
				name);
		return settingSpec.newSettingWithParsableValue(value);
	}
	
	public static Setting<Object> newInstanceWithParsableValue(
			final String name, final String value, final String doc) {
		Setting<Object> setting = newInstanceWithParsableValue(name, value);
		return new Setting<Object>(
				setting.getSettingSpec(), setting.getValue(), doc);
	}
	
	private final String name;
	private final SettingSpec<V> settingSpec;
	private final V value;
	private final String doc;
		
	Setting(final SettingSpec<V> spec, final V val) {
		this(spec, val, null);
	}
	
	Setting(final SettingSpec<V> spec, final V val, final String d) {
		V v = spec.getValueType().cast(val);
		this.name = spec.getName();
		this.settingSpec = spec;
		this.value = v;
		this.doc = d;		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Setting<?> other = (Setting<?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public String getDoc() {
		return this.doc;
	}
	
	public String getName() {
		return this.name;
	}
	
	public SettingSpec<V> getSettingSpec() {
		return this.settingSpec;
	}

	public V getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.name, this.value);
	}
	
}
