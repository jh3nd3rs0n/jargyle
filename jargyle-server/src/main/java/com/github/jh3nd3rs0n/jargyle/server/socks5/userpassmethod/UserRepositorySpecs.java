package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class UserRepositorySpecs {

	private final List<UserRepositorySpec> userRepositorySpecs;
	private final Map<String, UserRepositorySpec> userRepositorySpecsMap;
	
	public UserRepositorySpecs() {
		this.userRepositorySpecs = new ArrayList<UserRepositorySpec>();
		this.userRepositorySpecsMap = new HashMap<String, UserRepositorySpec>();
	}
	
	public UserRepositorySpec addThenGet(final UserRepositorySpec value) {
		this.userRepositorySpecs.add(value);
		this.userRepositorySpecsMap.put(value.getTypeName(), value);
		return value;
	}
	
	public List<UserRepositorySpec> toList() {
		return Collections.unmodifiableList(this.userRepositorySpecs);
	}
	
	public Map<String, UserRepositorySpec> toMap() {
		return Collections.unmodifiableMap(this.userRepositorySpecsMap);
	}

}
