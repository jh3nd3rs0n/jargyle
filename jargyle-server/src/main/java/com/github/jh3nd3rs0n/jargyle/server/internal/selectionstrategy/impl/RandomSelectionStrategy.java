package com.github.jh3nd3rs0n.jargyle.server.internal.selectionstrategy.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategySpec;

public final class RandomSelectionStrategy extends SelectionStrategy {
	
	private final ReentrantLock lock;
	private final SecureRandom secureRandom;
	
	public RandomSelectionStrategy(final SelectionStrategySpec spec) {
		super(spec);
		this.lock = new ReentrantLock();
		this.secureRandom = new SecureRandom();
	}
	
	public <T> T selectFrom(final List<? extends T> list) {
		T selected = null;
		this.lock.lock();
		try {
			selected = list.get(this.secureRandom.nextInt(list.size()));
		} finally {
			this.lock.unlock();
		}
		return selected;		
	}
}
