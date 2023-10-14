package com.github.jh3nd3rs0n.jargyle.client;

public abstract class Scheme {
	
	private final String string;
	
	Scheme(final String str) {
		this.string = str;
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
		Scheme other = (Scheme) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}

	public abstract SocksServerUri newSocksServerUri(
			final String host, final Integer port);
	
	@Override
	public String toString() {
		return this.string;
	}

}