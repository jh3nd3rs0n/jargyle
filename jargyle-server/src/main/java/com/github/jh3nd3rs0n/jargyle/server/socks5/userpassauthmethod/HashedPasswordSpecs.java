package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class HashedPasswordSpecs {

	private final List<HashedPasswordSpec> hashedPasswordSpecs;
	private final Map<String, HashedPasswordSpec> hashedPasswordSpecsMap;
	
	public HashedPasswordSpecs() {
		this.hashedPasswordSpecs = new ArrayList<HashedPasswordSpec>();
		this.hashedPasswordSpecsMap = new HashMap<String, HashedPasswordSpec>();
	}
	
	public HashedPasswordSpec addThenGet(final HashedPasswordSpec value) {
		this.hashedPasswordSpecs.add(value);
		this.hashedPasswordSpecsMap.put(value.getTypeName(), value);
		return value;
	}
	
	public List<HashedPasswordSpec> toList() {
		return Collections.unmodifiableList(this.hashedPasswordSpecs);
	}
	
	public Map<String, HashedPasswordSpec> toMap() {
		return Collections.unmodifiableMap(this.hashedPasswordSpecsMap);
	}

}
