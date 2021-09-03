package com.github.jh3nd3rs0n.jargyle.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class SocketSettingSpecs {

	private final List<SocketSettingSpec<Object>> socketSettingSpecs;
	
	public SocketSettingSpecs() {
		this.socketSettingSpecs = new ArrayList<SocketSettingSpec<Object>>();
	}
	
	public <T> SocketSettingSpec<T> addThenGet(
			final SocketSettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> val = (SocketSettingSpec<Object>) value;
		this.socketSettingSpecs.add(val);
		return value;		
	}
	
	public List<SocketSettingSpec<Object>> toList() {
		return Collections.unmodifiableList(this.socketSettingSpecs);
	}
	
}
