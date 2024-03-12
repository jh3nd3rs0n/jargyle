package com.github.jh3nd3rs0n.jargyle.server;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;

public final class NonNegativeIntegerLimit {

	public static NonNegativeIntegerLimit newInstance(
			final NonNegativeInteger value) {
		return new NonNegativeIntegerLimit(value);
	}
	
	public static NonNegativeIntegerLimit newInstanceFrom(final String s) {
		return newInstance(NonNegativeInteger.valueOf(s));
	}
	
	private final AtomicInteger currentCount;
	private final NonNegativeInteger nonNegativeIntegerValue;
	private final Semaphore semaphore;
	
	private NonNegativeIntegerLimit(final NonNegativeInteger value) {
		this.currentCount = new AtomicInteger(0);
		this.nonNegativeIntegerValue = value;
		this.semaphore = new Semaphore(value.intValue(), true);
	}
	
	public int currentCount() {
		return this.currentCount.intValue();
	}
	
	public void decrementCurrentCount() {
		if (this.currentCount.intValue() == 0) {
			throw new IllegalStateException(
					"cannot decrement current count below zero");
		}
		this.currentCount.decrementAndGet();
		this.semaphore.release();
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
		NonNegativeIntegerLimit other = (NonNegativeIntegerLimit) obj;
		if (this.nonNegativeIntegerValue == null) {
			if (other.nonNegativeIntegerValue != null) {
				return false;
			}
		} else if (!this.nonNegativeIntegerValue.equals(
				other.nonNegativeIntegerValue)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nonNegativeIntegerValue == null) ?
				0 : this.nonNegativeIntegerValue.hashCode());
		return result;
	}

	public NonNegativeInteger nonNegativeIntegerValue() {
		return this.nonNegativeIntegerValue;
	}
	
	@Override
	public String toString() {
		return this.nonNegativeIntegerValue.toString();
	}

	public boolean tryIncrementCurrentCount() {
		if (this.semaphore.tryAcquire()) {
			this.currentCount.incrementAndGet();
			return true;
		}
		return false;
	}
	
}
