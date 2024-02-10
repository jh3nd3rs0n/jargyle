package com.github.jh3nd3rs0n.echo.performance.test;

public final class LoadTestRunnerResults {

    private final ThreadsRunnerResults lastSuccessfulThreadsRunnerResults;
    private final ThreadsRunnerResults unsuccessfulThreadsRunnerResults;

    LoadTestRunnerResults(
            final ThreadsRunnerResults lastSuccessfulResults,
            final ThreadsRunnerResults unsuccessfulResults) {
        this.lastSuccessfulThreadsRunnerResults = lastSuccessfulResults;
        this.unsuccessfulThreadsRunnerResults = unsuccessfulResults;
    }

    public ThreadsRunnerResults getLastSuccessfulThreadsRunnerResults() {
        return this.lastSuccessfulThreadsRunnerResults;
    }

    public ThreadsRunnerResults getUnsuccessfulThreadsRunnerResults() {
        return this.unsuccessfulThreadsRunnerResults;
    }

}
