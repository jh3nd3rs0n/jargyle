package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

@SingleValueTypeDoc(
		description = "",
		name = "Selection Strategy",
		singleValueSpecs = {
				SelectionStrategySpecConstants.class
		},
		syntax = "CYCLICAL|RANDOM",
		syntaxName = "SELECTION_STRATEGY"
)
public abstract class SelectionStrategy {
	
	public static SelectionStrategy newInstanceFrom(final String s) {
		SelectionStrategySpec selectionStrategySpec = 
				SelectionStrategySpecConstants.valueOfName(s);
		return selectionStrategySpec.newSelectionStrategy();
	}
	
	private final String name;
	private final SelectionStrategySpec selectionStrategySpec;
	
	public SelectionStrategy(final SelectionStrategySpec spec) {
		this.name = spec.getName();
		this.selectionStrategySpec = spec;
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
		SelectionStrategy other = (SelectionStrategy) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	public final String getName() {
		return this.name;
	}

	public final SelectionStrategySpec getSelectionStrategySpec() {
		return this.selectionStrategySpec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		return result;
	}

	public abstract <T> T selectFrom(final List<? extends T> list);
	
	@Override
	public final String toString() {
		return this.name;
	}
	
}
