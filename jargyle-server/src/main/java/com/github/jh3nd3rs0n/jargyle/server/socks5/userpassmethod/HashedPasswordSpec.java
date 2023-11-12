package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod;

import java.util.Objects;

public abstract class HashedPasswordSpec {

	private final String typeName;
	
	HashedPasswordSpec(final String typName) {
		Objects.requireNonNull(typName);
		this.typeName = typName;
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
		HashedPasswordSpec other = (HashedPasswordSpec) obj;
		if (this.typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!this.typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}
	
	public final String getTypeName() {
		return this.typeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.typeName == null) ? 
				0 : this.typeName.hashCode());
		return result;
	}

	public abstract HashedPassword newHashedPassword(final char[] password);
	
	public abstract HashedPassword newHashedPassword(
			final String argumentsString);

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [typeName=")
			.append(this.typeName)
			.append("]");
		return builder.toString();
	}

}
