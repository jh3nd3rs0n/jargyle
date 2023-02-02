package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class IoStressor {
	
	public static final class Stats {
		
		private final AtomicInteger actualSuccessfulThreadCount;
		private final AtomicInteger actualThreadCount;
		private final Set<Long> completedTimes;
		private final Set<String> exceptions;
		private final ReentrantLock lock;
		private final int threadCount;
		
		public Stats(final int threadCount) {
			this.actualSuccessfulThreadCount = new AtomicInteger(0);
			this.actualThreadCount = new AtomicInteger(0);
			this.completedTimes = new TreeSet<Long>();
			this.exceptions = new TreeSet<String>();
			this.lock = new ReentrantLock();
			this.threadCount = threadCount;
		}
		
		public void addCompletedTime(final long completedTime) {
			this.lock.lock();
			try {
				this.completedTimes.add(Long.valueOf(completedTime));
			} finally {
				this.lock.unlock();
			}
		}
		
		public void addException(final String exception) {
			this.lock.lock();
			try {
				this.exceptions.add(exception);
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
		
		public Set<Long> getCompletedTimes() {
			return Collections.unmodifiableSet(this.completedTimes);
		}
		
		public Set<String> getExceptions() {
			return Collections.unmodifiableSet(this.exceptions);
		}
		
		public int getThreadCount() {
			return this.threadCount;
		}
		
		public void incrementActualSuccessfulThreadCount() {
			this.actualSuccessfulThreadCount.incrementAndGet();
		}
		
		public void incrementActualThreadCount() {
			this.actualThreadCount.incrementAndGet();
		}
		
		public void print() {
			List<Long> completedTimesList = 
					this.completedTimes.stream().sorted().collect(Collectors.toList());
			int completedTimesListSize = completedTimesList.size();
			long combinedTime = 0L;
			for (Long completedTime : completedTimesList) {
				combinedTime += completedTime.longValue();
			}
			long shortestTime = (completedTimesListSize > 0) ? 
					completedTimesList.get(0).longValue() : 0L;
			long averageTime = (completedTimesListSize > 0) ? 
					Long.valueOf(combinedTime / completedTimesListSize).longValue() : 0L;
			long longestTime = (completedTimesListSize > 0) ? 
					completedTimesList.get(completedTimesListSize - 1).longValue() : 0L;
			System.out.printf("# of threads: %s%n", this.threadCount);
			System.out.printf("Actual # of threads: %s%n", this.actualThreadCount);
			System.out.printf("Actual # of successful threads: %s%n", 
					this.actualSuccessfulThreadCount);
			System.out.printf("Common IOExceptions: %n");
			this.exceptions.stream().sorted().forEach(e -> {
				System.out.println(e);
			});
			System.out.printf("Combined time: %s ms%n", combinedTime);
			System.out.printf("Shortest time: %s ms%n", shortestTime);
			System.out.printf("Average time: %s ms%n", averageTime);
			System.out.printf("Longest time: %s ms%n", longestTime);
		}
		
	}
	
	private final int threadCount;
	
	public IoStressor(final int thrdCount) {
		this.threadCount = thrdCount;
	}
	
	public Stats executeForEachThread(final IoRunnable ioRunnable) {
		Stats stats = new Stats(this.threadCount);
		ExecutorService executor = ExecutorHelper.newExecutor();
		try {
			for (int i = 0; i < this.threadCount; i++) {
				executor.execute(() -> {
					stats.incrementActualThreadCount();
					long startTime = System.currentTimeMillis();
					IOException ioe = null;
					try {
						ioRunnable.run();
					} catch (IOException e) {
						ioe = e;
					}
					long endTime = System.currentTimeMillis();
					long completedTime = endTime - startTime;
					if (ioe != null) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						ioe.printStackTrace(pw);
						pw.flush();
						stats.addException(sw.toString());
					}
					stats.addCompletedTime(Long.valueOf(completedTime));
					if (ioe == null) {
						stats.incrementActualSuccessfulThreadCount();
					}					
				});
			}
		} finally {
			executor.shutdown();
			try {
				if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				executor.shutdownNow();
			}
		}
		return stats;
	}

}
