package com.github.jh3nd3rs0n.jargyle.server;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;

public final class NonnegativeIntegerLimit {

	public static NonnegativeIntegerLimit newInstanceOf(
			final NonnegativeInteger value) {
		return new NonnegativeIntegerLimit(value);
	}
	
	public static NonnegativeIntegerLimit newInstanceOf(final String s) {
		return newInstanceOf(NonnegativeInteger.newInstanceOf(s));
	}
	
	private final AtomicInteger currentCount;
	private final NonnegativeInteger nonnegativeIntegerValue;
	private final Semaphore semaphore;
	
	private NonnegativeIntegerLimit(final NonnegativeInteger value) {
		this.currentCount = new AtomicInteger(0);
		this.nonnegativeIntegerValue = value;
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
		NonnegativeIntegerLimit other = (NonnegativeIntegerLimit) obj;
		if (this.nonnegativeIntegerValue == null) {
			if (other.nonnegativeIntegerValue != null) {
				return false;
			}
		} else if (!this.nonnegativeIntegerValue.equals(
				other.nonnegativeIntegerValue)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nonnegativeIntegerValue == null) ? 
				0 : this.nonnegativeIntegerValue.hashCode());
		return result;
	}

	public NonnegativeInteger nonnegativeIntegerValue() {
		return this.nonnegativeIntegerValue;
	}
	
	@Override
	public String toString() {
		return this.nonnegativeIntegerValue.toString();
	}

	public boolean tryIncrementCurrentCount() {
		if (this.semaphore.tryAcquire()) {
			this.currentCount.incrementAndGet();
			return true;
		}
		return false;
	}
	
}
