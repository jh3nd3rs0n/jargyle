package com.github.jh3nd3rs0n.jargyle.server;

import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;

public final class NonnegativeIntegerLimit {

	public static NonnegativeIntegerLimit newInstance(
			final NonnegativeInteger value) {
		return new NonnegativeIntegerLimit(value);
	}
	
	public static NonnegativeIntegerLimit newInstance(final String s) {
		return newInstance(NonnegativeInteger.newInstance(s));
	}
	
	private int currentCount;
	private final ReentrantLock lock;
	private final NonnegativeInteger nonnegativeIntegerValue;
	
	private NonnegativeIntegerLimit(final NonnegativeInteger value) {
		this.currentCount = 0;
		this.lock = new ReentrantLock();
		this.nonnegativeIntegerValue = value;
	}
	
	public void decrementCurrentCount() {
		this.lock.lock();
		try {
			if (this.currentCount == 0) {
				throw new IllegalStateException(
						"cannot decrement current count below zero");
			}
			this.currentCount--;
		} finally {
			this.lock.unlock();
		}
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
	
	public boolean hasBeenReached() {
		boolean hasBeenReached = false;
		this.lock.lock();
		try {
			hasBeenReached = 
					this.currentCount >= this.nonnegativeIntegerValue.intValue(); 
		} finally {
			this.lock.unlock();
		}
		return hasBeenReached;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.nonnegativeIntegerValue == null) ? 
				0 : this.nonnegativeIntegerValue.hashCode());
		return result;
	}
	
	public void incrementCurrentCount() {
		this.lock.lock();
		try {
			if (this.currentCount >= this.nonnegativeIntegerValue.intValue()) {
				throw new IllegalStateException(
						"cannot increment current count any further");
			}
			this.currentCount++;			
		} finally {
			this.lock.unlock();
		}
	}

	public NonnegativeInteger nonnegativeIntegerValue() {
		return this.nonnegativeIntegerValue;
	}

	@Override
	public String toString() {
		return this.nonnegativeIntegerValue.toString();
	}
	
}
