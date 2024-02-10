package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public final class ThreadsRunner {

	private static final long DEFAULT_TIMEOUT = 1;
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

	private long timeout;
	private TimeUnit timeUnit;

	public ThreadsRunner() {
		this.timeout = DEFAULT_TIMEOUT;
		this.timeUnit = DEFAULT_TIME_UNIT;
	}

	public ThreadsRunner setTimeout(final long tmt) {
		this.timeout = tmt;
		return this;
	}

	public ThreadsRunner setTimeUnit(final TimeUnit unit) {
		this.timeUnit = unit;
		return this;
	}

	public ThreadsRunnerResults run(final Runnable runnable, final int threadCount) {
		ThreadsRunnerResults threadsRunnerResults = new ThreadsRunnerResults(threadCount);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			for (int i = 0; i < threadCount; i++) {
				executor.execute(new RunnableImpl(threadsRunnerResults, runnable));
			}
		} finally {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(this.timeout, this.timeUnit)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
			}
		}
		return threadsRunnerResults;
	}

	private static final class RunnableImpl implements Runnable {

		private final ThreadsRunnerResults threadsRunnerResults;
		private final Runnable runnable;

		public RunnableImpl(final ThreadsRunnerResults perfStats, final Runnable r) {
			this.threadsRunnerResults = perfStats;
			this.runnable = r;
		}

		@Override
		public void run() {
			this.threadsRunnerResults.incrementActualThreadCount();
			long startTime = System.currentTimeMillis();
			Throwable throwable = null;
			try {
				this.runnable.run();
			} catch (Throwable t) {
				throwable = t;
			}
			long endTime = System.currentTimeMillis();
			long completedTime = endTime - startTime;
			if (throwable != null) {
				this.threadsRunnerResults.addThrowable(throwable);
			}
			this.threadsRunnerResults.addCompletedTime(completedTime);
			if (throwable == null) {
				this.threadsRunnerResults.incrementActualSuccessfulThreadCount();
			}
		}

	}

}
