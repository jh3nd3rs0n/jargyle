package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public abstract class SelectionStrategy {

	private static final class CyclicalSelectionStrategy extends SelectionStrategy {

		private int index;
		private final ReentrantLock lock;
		
		private CyclicalSelectionStrategy(final String str, final boolean mut) {
			super(str, mut);
			this.index = 0;
			this.lock = new ReentrantLock();
		}
		
		@Override
		protected <T> T implSelectFrom(final List<? extends T> list) {
			T selected = null;
			this.lock.lock();
			try {
				if (this.index > list.size() - 1) {
					this.index = 0;
				}
				selected = list.get(this.index);
				this.index++;
			} finally {
				this.lock.unlock();
			}
			return selected;
		}

		@Override
		public SelectionStrategy newMutableInstance() {
			return new CyclicalSelectionStrategy(this.toString(), true);
		}		
		
	}
	
	private static final class RandomSelectionStrategy extends SelectionStrategy {

		private final ReentrantLock lock;
		private final Random random;
		
		private RandomSelectionStrategy(final String str, final boolean mut) {
			super(str, mut);
			this.lock = new ReentrantLock();
			this.random = (mut) ? new Random() : null;
		}

		@Override
		protected <T> T implSelectFrom(final List<? extends T> list) {
			T selected = null;
			this.lock.lock();
			try {
				selected = list.get(this.random.nextInt(list.size()));
			} finally {
				this.lock.unlock();
			}
			return selected;
		}

		@Override
		public SelectionStrategy newMutableInstance() {
			return new RandomSelectionStrategy(this.toString(), true);
		}
		
	}
	
	private static final List<SelectionStrategy> VALUES = new ArrayList<SelectionStrategy>();
	private static final Map<String, SelectionStrategy> VALUES_MAP = new HashMap<String, SelectionStrategy>();
	
	@HelpText(
			doc = "Select the next in the cycle", 
			usage = "CYCLICAL"
	)
	public static final SelectionStrategy CYCLICAL = new CyclicalSelectionStrategy("CYCLICAL", false);
	
	@HelpText(
			doc = "Select the next at random", 
			usage = "RANDOM"
	)
	public static final SelectionStrategy RANDOM = new RandomSelectionStrategy("RANDOM", false);

	public static SelectionStrategy valueOf(final String s) {
		if (!VALUES_MAP.containsKey(s)) {
			String str = VALUES.stream()
					.map(SelectionStrategy::toString)
					.collect(Collectors.joining(", "));
			throw new IllegalArgumentException(String.format(
					"expected selection strategy must be one of the "
					+ "following values: %s. actual value is %s",
					str,
					s));
		}
		return VALUES_MAP.get(s);
	}
	
	public static List<SelectionStrategy> values() {
		return Collections.unmodifiableList(VALUES);
	}
	
	private final boolean mutable;
	private final String string;
	
	SelectionStrategy(final String str, final boolean mut) {
		if (!mut) {
			VALUES.add(this);
			VALUES_MAP.put(str, this);
		}
		this.mutable = mut;
		this.string = str;
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
		SelectionStrategy other = (SelectionStrategy) obj;
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
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}

	protected abstract <T> T implSelectFrom(final List<? extends T> list);
	
	public final boolean isMutable() {
		return this.mutable;
	}
	
	public abstract SelectionStrategy newMutableInstance();
	
	public final <T> T selectFrom(final List<? extends T> list) {
		if (!this.mutable) {
			throw new IllegalStateException("SelectionStrategy is not mutable");
		}
		if (list.size() == 0) {
			return null;
		}
		return this.implSelectFrom(list);
	}
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
