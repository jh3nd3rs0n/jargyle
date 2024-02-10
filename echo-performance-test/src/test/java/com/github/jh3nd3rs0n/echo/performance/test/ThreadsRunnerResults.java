package com.github.jh3nd3rs0n.echo.performance.test;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public final class ThreadsRunnerResults {

    private final AtomicInteger actualSuccessfulThreadCount;
    private final AtomicInteger actualThreadCount;
    private final SortedSet<Long> completedTimes;
    private final ReentrantLock lock;
    private final int threadCount;
    private final SortedSet<Throwable> throwables;

    public ThreadsRunnerResults(final int threadCount) {
        this.actualSuccessfulThreadCount = new AtomicInteger(0);
        this.actualThreadCount = new AtomicInteger(0);
        this.completedTimes = new TreeSet<>();
        this.lock = new ReentrantLock();
        this.threadCount = threadCount;
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

    public int getActualSuccessfulThreadCount() {
        return this.actualSuccessfulThreadCount.intValue();
    }

    public int getActualThreadCount() {
        return this.actualThreadCount.intValue();
    }

    public SortedSet<Long> getCompletedTimes() {
        return Collections.unmodifiableSortedSet(this.completedTimes);
    }

    public int getThreadCount() {
        return this.threadCount;
    }

    public SortedSet<Throwable> getThrowables() {
        return Collections.unmodifiableSortedSet(this.throwables);
    }

    public void incrementActualSuccessfulThreadCount() {
        this.actualSuccessfulThreadCount.incrementAndGet();
    }

    public void incrementActualThreadCount() {
        this.actualThreadCount.incrementAndGet();
    }

}
