package com.github.jh3nd3rs0n.jargyle.server;

import java.util.concurrent.atomic.AtomicInteger;

import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;

public final class NonnegativeIntegerLimit {

	public static NonnegativeIntegerLimit newInstance(
			final NonnegativeInteger value) {
		return new NonnegativeIntegerLimit(value);
	}
	
	public static NonnegativeIntegerLimit newInstance(final String s) {
		return newInstance(NonnegativeInteger.newInstance(s));
	}
	
	private AtomicInteger currentCount;
	private final NonnegativeInteger nonnegativeIntegerValue;
	
	private NonnegativeIntegerLimit(final NonnegativeInteger value) {
		this.currentCount = new AtomicInteger(0);
		this.nonnegativeIntegerValue = value;
	}
	
	public synchronized void decrementCurrentCount() {
		if (this.currentCount.intValue() == 0) {
			throw new IllegalStateException(
					"cannot decrement current count below zero");
		}
		this.currentCount.decrementAndGet();
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
	
	public synchronized boolean hasBeenReached() {
		return this.currentCount.intValue() >= this.nonnegativeIntegerValue.intValue();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nonnegativeIntegerValue == null) ? 
				0 : this.nonnegativeIntegerValue.hashCode());
		return result;
	}
	
	public synchronized void incrementCurrentCount() {
		this.currentCount.incrementAndGet();
	}

	public NonnegativeInteger nonnegativeIntegerValue() {
		return this.nonnegativeIntegerValue;
	}

	@Override
	public String toString() {
		return this.nonnegativeIntegerValue.toString();
	}
	
}
