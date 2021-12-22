package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class Selector<T> {

	private static final class CyclicalSelector<T> extends Selector<T> {

		private int index;
		
		private CyclicalSelector(final List<? extends T> l) {
			super(l);
			this.index = 0;
		}
		
		@Override
		public T select() {
			T selected = null;
			List<T> list = this.list();
			if (list.size() == 0) {
				return selected;
			}
			synchronized (this) {
				if (this.index > list.size() - 1) {
					this.index = 0;
				}
				selected = list.get(this.index);
				this.index++;
			}
			return selected;
		}
		
	}
	
	private static final class RandomSelector<T> extends Selector<T> {

		private final Random random;
		
		private RandomSelector(final List<? extends T> l) {
			super(l);
			this.random = new Random();
		}
		
		@Override
		public T select() {
			T selected = null;
			List<T> list = this.list();
			if (list.size() == 0) { 
				return selected; 
			}
			synchronized (this) {
				selected = list.get(this.random.nextInt(list.size()));
			}
			return selected;
		}
		
	}
	
	public static <T> Selector<T> newCyclicalSelector(
			final List<? extends T> list) {
		return new CyclicalSelector<T>(list);
	}
	
	public static <T> Selector<T> newRandomSelector(
			final List<? extends T> list) {
		return new RandomSelector<T>(list);
	}

	private final List<T> list;
	
	private Selector(final List<? extends T> l) { 
		this.list = new ArrayList<T>(l);
	}
	
	public final List<T> list() {
		return Collections.unmodifiableList(this.list);
	}
	
	public abstract T select();
	
}
