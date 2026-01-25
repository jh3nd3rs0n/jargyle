package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints.load;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.SortedSet;

public final class LoadTestReportHelper {

    private LoadTestReportHelper() {
    }

    public static Path createLoadTestReport(
            final String filename, final String header) throws IOException {
        Path loadTestReportsDir = new File(
                System.getProperty("load.test.reports.directory")).toPath();
        if (Files.notExists(loadTestReportsDir)) {
            Files.createDirectory(loadTestReportsDir);
        }
        Path loadTestReport = loadTestReportsDir.resolve(filename);
        Files.deleteIfExists(loadTestReport);
        Files.createFile(loadTestReport);
        Files.write(
                loadTestReport,
                String.format("%s:", header).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        return loadTestReport;
    }

    private static String print(
            final LoadTestRunnerResults loadTestRunnerResults) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.printf("%n    # of threads: %s",
                loadTestRunnerResults.getThreadCount());
        printWriter.printf("%n    # of completed threads: %s",
                loadTestRunnerResults.getCompletedThreadCount());
        printWriter.printf("%n    # of successful completed threads: %s",
                loadTestRunnerResults.getSuccessfulCompletedThreadCount());
        SortedSet<Throwable> throwables = loadTestRunnerResults.getThrowables();
        if (!throwables.isEmpty()) {
            printWriter.printf("%n    Common Throwables:");
            /*
             * Not using the Stream API in order to avoid an intermittent
             * ConcurrentModificationException to be thrown.
             */
            String lineSeparator = System.lineSeparator();
            String indent = "      ";
            for (Throwable throwable : throwables) {
                String stackTraceString = ThrowableHelper.toStackTraceString(
                        throwable);
                String indentedStackTraceString =
                        lineSeparator + indent + stackTraceString.replace(
                                lineSeparator,
                                lineSeparator + indent);
                printWriter.print(indentedStackTraceString);
            }
        }
        printWriter.printf("%n    Delay between threads starting: %s ms",
                loadTestRunnerResults.getDelayBetweenThreadsStarting());
        SortedSet<Long> completedTimes =
                loadTestRunnerResults.getCompletedTimes();
        int completedTimesCount = completedTimes.size();
        long combinedTime = 0L;
        for (Long completedTime : completedTimes) {
            combinedTime += completedTime;
        }
        long shortestTime = (completedTimesCount > 0) ?
                completedTimes.first() : 0L;
        long averageTime = (completedTimesCount > 0) ?
                combinedTime / completedTimesCount : 0L;
        long longestTime = (completedTimesCount > 0) ?
                completedTimes.last() : 0L;
        printWriter.printf("%n    Shortest thread time: %s ms", shortestTime);
        printWriter.printf("%n    Average thread time: %s ms", averageTime);
        printWriter.printf("%n    Longest thread time: %s ms", longestTime);
        printWriter.flush();
        return stringWriter.toString();
    }

    public static void writeToLoadTestReport(
            final Path loadTestReport,
            final String header,
            final LoadTestRunnerResults loadTestRunnerResults)
            throws IOException {
        Files.write(
                loadTestReport,
                String.format("%n  %s:", header).getBytes(
                        StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        Files.write(
                loadTestReport,
                print(loadTestRunnerResults).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
    }

}
