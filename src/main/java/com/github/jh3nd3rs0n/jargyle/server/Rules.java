package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class Rules<R extends Rule> {

	private final List<R> rules;
	
	protected Rules(final List<? extends R> rls) {
		this.rules = new ArrayList<R>(rls);
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Rules<?> other = (Rules<?>) obj;
		if (this.rules == null) {
			if (other.rules != null) {
				return false;
			}
		} else if (!this.rules.equals(other.rules)) {
			return false;
		}
		return true;
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.rules == null) ? 
				0 : this.rules.hashCode());
		return result;
	}

	public final List<R> toList() {
		return Collections.unmodifiableList(this.rules);
	}

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<R> iterator = this.rules.iterator();
				iterator.hasNext();) {
			R rule = iterator.next();
			builder.append(rule);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
	
}
