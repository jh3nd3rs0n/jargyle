package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.util.Objects;

public abstract class SettingSpec<V> {
	
	private Setting<V> defaultSetting;
	private final V defaultValue;
	private final String string;
	private final Class<V> valueType;
		
	public SettingSpec( 
			final Object permission, 
			final String s, 
			final Class<V> valType, 
			final V defaultVal) {
		Objects.requireNonNull(permission);
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		if (!NewSettingSpecPermission.INSTANCE.equals(permission)) {
			throw new IllegalArgumentException(
					"invalid new SettingSpec permission");
		}		
		this.defaultValue = valType.cast(defaultVal);
		this.string = s;
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
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
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
	
	public final Class<V> getValueType() {
		return this.valueType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}

	public Setting<V> newSetting(final V value) {
		return new Setting<V>(this, this.valueType.cast(value));
	}
	
	public abstract Setting<V> newSettingOfParsableValue(final String value);
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
