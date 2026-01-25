package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

final class LoadTestRunnerWorker implements Runnable {

    private final long delayStart;
    private final LoadTestRunnerResults loadTestRunnerResults;
    private final Runnable runnable;

    public LoadTestRunnerWorker(
            final long delay,
            final Runnable r,
            final LoadTestRunnerResults results) {
        this.delayStart = delay;
        this.loadTestRunnerResults = results;
        this.runnable = r;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.delayStart);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
            this.loadTestRunnerResults.addThrowable(throwable);
        }
        this.loadTestRunnerResults.addCompletedTime(completedTime);
        this.loadTestRunnerResults.incrementCompletedThreadCount();
        if (throwable == null) {
            this.loadTestRunnerResults.incrementSuccessfulCompletedThreadCount();
        }
    }

}
