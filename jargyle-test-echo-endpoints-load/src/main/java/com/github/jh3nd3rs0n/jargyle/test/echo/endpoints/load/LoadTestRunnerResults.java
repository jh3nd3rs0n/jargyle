package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public final class LoadTestRunnerResults {

    private final AtomicInteger completedThreadCount;
    private final SortedSet<Long> completedTimes;
    private final long delayBetweenThreadsStarting;
    private final ReentrantLock lock;
    private final AtomicInteger successfulCompletedThreadCount;
    private final int threadCount;
    private final SortedSet<Throwable> throwables;

    public LoadTestRunnerResults(
            final int numberOfThreads, final long delayBetweenThreadsStart) {
        this.completedThreadCount = new AtomicInteger(0);
        this.completedTimes = new TreeSet<>();
        this.delayBetweenThreadsStarting = delayBetweenThreadsStart;
        this.lock = new ReentrantLock();
        this.successfulCompletedThreadCount = new AtomicInteger(0);
        this.threadCount = numberOfThreads;
        this.throwables = new TreeSet<>(Comparator.comparing(
                ThrowableHelper::toStackTraceString));
    }

    public void addCompletedTime(final long completedTime) {
        this.lock.lock();
        try {
            this.completedTimes.add(completedTime);
        } finally {
            this.lock.unlock();
        }
    }

    public void addThrowable(final Throwable throwable) {
        this.lock.lock();
        try {
            this.throwables.add(throwable);
        } finally {
            this.lock.unlock();
        }
    }

    public int getCompletedThreadCount() {
        return this.completedThreadCount.intValue();
    }

    public SortedSet<Long> getCompletedTimes() {
        return Collections.unmodifiableSortedSet(this.completedTimes);
    }

    public long getDelayBetweenThreadsStarting() {
        return this.delayBetweenThreadsStarting;
    }

    public int getSuccessfulCompletedThreadCount() {
        return this.successfulCompletedThreadCount.intValue();
    }

    public int getThreadCount() {
        return this.threadCount;
    }

    public SortedSet<Throwable> getThrowables() {
        return Collections.unmodifiableSortedSet(this.throwables);
    }

    public void incrementCompletedThreadCount() {
        this.completedThreadCount.incrementAndGet();
    }

    public void incrementSuccessfulCompletedThreadCount() {
        this.successfulCompletedThreadCount.incrementAndGet();
    }

}
