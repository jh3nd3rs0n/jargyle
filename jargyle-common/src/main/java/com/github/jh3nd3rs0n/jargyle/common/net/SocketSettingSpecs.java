package com.github.jh3nd3rs0n.jargyle.common.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SocketSettingSpecs {

	private final List<SocketSettingSpec<Object>> socketSettingSpecs;
	private final Map<String, SocketSettingSpec<Object>> socketSettingSpecsMap;
	
	public SocketSettingSpecs() {
		this.socketSettingSpecs = new ArrayList<SocketSettingSpec<Object>>();
		this.socketSettingSpecsMap = 
				new HashMap<String, SocketSettingSpec<Object>>();
	}
	
	public <T> SocketSettingSpec<T> addThenGet(
			final SocketSettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> val = (SocketSettingSpec<Object>) value;
		this.socketSettingSpecs.add(val);
		this.socketSettingSpecsMap.put(val.getName(), val);
		return value;		
	}
	
	public List<SocketSettingSpec<Object>> toList() {
		return Collections.unmodifiableList(this.socketSettingSpecs);
	}
	
	public Map<String, SocketSettingSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.socketSettingSpecsMap);
	}
	
}
