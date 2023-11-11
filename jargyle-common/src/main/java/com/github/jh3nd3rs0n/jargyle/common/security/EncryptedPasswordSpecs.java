package com.github.jh3nd3rs0n.jargyle.common.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class EncryptedPasswordSpecs {

	private final List<EncryptedPasswordSpec> encryptedPasswordSpecs;
	private final Map<String, EncryptedPasswordSpec> encryptedPasswordSpecsMap;
	
	public EncryptedPasswordSpecs() {
		this.encryptedPasswordSpecs = new ArrayList<EncryptedPasswordSpec>();
		this.encryptedPasswordSpecsMap = 
				new HashMap<String, EncryptedPasswordSpec>();
	}
	
	public EncryptedPasswordSpec addThenGet(final EncryptedPasswordSpec value) {
		this.encryptedPasswordSpecs.add(value);
		this.encryptedPasswordSpecsMap.put(value.getTypeName(), value);
		return value;
	}
	
	public List<EncryptedPasswordSpec> toList() {
		return Collections.unmodifiableList(this.encryptedPasswordSpecs);
	}
	
	public Map<String, EncryptedPasswordSpec> toMap() {
		return Collections.unmodifiableMap(this.encryptedPasswordSpecsMap);
	}

}
