package com.github.jh3nd3rs0n.jargyle.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Schemes {

	private final List<Scheme> schemes;
	private final Map<String, Scheme> schemesMap;
	
	public Schemes() {
		this.schemes = new ArrayList<Scheme>();
		this.schemesMap = new HashMap<String, Scheme>();
	}
	
	public Scheme addThenGet(final Scheme value) {
		this.schemes.add(value);
		this.schemesMap.put(value.toString(), value);
		return value;
	}
	
	public List<Scheme> toList() {
		return Collections.unmodifiableList(this.schemes);
	}
	
	public Map<String, Scheme> toMap() {
		return Collections.unmodifiableMap(this.schemesMap);
	}
	
}
