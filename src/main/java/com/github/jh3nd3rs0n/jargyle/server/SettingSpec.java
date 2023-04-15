package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

public abstract class SettingSpec<V> {
	
	private Setting<V> defaultSetting;
	private final V defaultValue;
	private final String name;
	private final Class<V> valueType;
		
	public SettingSpec(
			final String n, final Class<V> valType, final V defaultVal) {
		Objects.requireNonNull(n);
		Objects.requireNonNull(valType);
		this.defaultValue = valType.cast(defaultVal);
		this.name = n;
		this.valueType = valType;
		this.defaultSetting = null;
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
		SettingSpec<?> other = (SettingSpec<?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	public final Setting<V> getDefaultSetting() {
		if (this.defaultSetting == null) {
			this.defaultSetting = new Setting<V>(this, this.valueType.cast(
					this.defaultValue)); 
		}
		return this.defaultSetting;
	}
	
	public final String getName() {
		return this.name;
	}

	public final Class<V> getValueType() {
		return this.valueType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	public Setting<V> newSetting(final V value) {
		return new Setting<V>(this, this.valueType.cast(value));
	}
	
	public abstract Setting<V> newSettingOfParsableValue(final String value);
	
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [name=")
			.append(this.name)
			.append("]");
		return builder.toString();
	}
	
}
