package com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategySpec;

public final class CyclicalSelectionStrategy extends SelectionStrategy {

	private int index;
	private final ReentrantLock lock;
	
	public CyclicalSelectionStrategy(final SelectionStrategySpec spec) {
		super(spec);
		this.index = 0;
		this.lock = new ReentrantLock();
	}
	
	public <T> T selectFrom(final List<? extends T> list) {
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
	
}
