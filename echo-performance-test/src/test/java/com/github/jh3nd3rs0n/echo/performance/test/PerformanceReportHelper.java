package com.github.jh3nd3rs0n.echo.performance.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.SortedSet;

public final class PerformanceReportHelper {

    private PerformanceReportHelper() {
    }

    public static Path createPerformanceReport(
            final String filename, final String header) throws IOException {
        Path performanceReportsDir = new File(
                System.getProperty("performance.reports.directory")).toPath();
        if (Files.notExists(performanceReportsDir)) {
            Files.createDirectory(performanceReportsDir);
        }
        Path performanceReport = performanceReportsDir.resolve(filename);
        Files.deleteIfExists(performanceReport);
        Files.createFile(performanceReport);
        Files.write(
                performanceReport,
                String.format("%s:", header).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        return performanceReport;
    }

    private static String print(
            final ThreadsRunnerResults threadsRunnerResults) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.printf("      # of threads: %s%n",
                threadsRunnerResults.getThreadCount());
        printWriter.printf("      Actual # of threads: %s%n",
                threadsRunnerResults.getActualThreadCount());
        printWriter.printf("      Actual # of successful threads: %s%n",
                threadsRunnerResults.getActualSuccessfulThreadCount());
        SortedSet<Throwable> throwables = threadsRunnerResults.getThrowables();
        if (!throwables.isEmpty()) {
            printWriter.printf("      Common Throwables:%n");
            /*
             * Not using the Stream API in order to avoid an intermittent
             * ConcurrentModificationException to be thrown.
             */
            String indent = "        ";
            for (Throwable throwable : throwables) {
                String stackTraceString = ThrowableHelper.toStackTraceString(
                        throwable);
                String indentedStackTraceString =
                        indent + stackTraceString.replace(
                                System.lineSeparator(),
                                System.lineSeparator() + indent);
                printWriter.println(indentedStackTraceString);
            }
        }
        SortedSet<Long> completedTimes =
                threadsRunnerResults.getCompletedTimes();
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
        printWriter.printf("      Combined time: %s ms%n", combinedTime);
        printWriter.printf("      Shortest time: %s ms%n", shortestTime);
        printWriter.printf("      Average time: %s ms%n", averageTime);
        printWriter.printf("      Longest time: %s ms", longestTime);
        printWriter.flush();
        return stringWriter.toString();
    }

    public static void writeToPerformanceReport(
            final Path performanceReport,
            final String header,
            final ThreadsRunnerResults lastSuccessfulThreadsRunnerResults,
            final ThreadsRunnerResults unsuccessfulThreadsRunnerResults)
            throws IOException {
        Files.write(
                performanceReport,
                String.format("%n  %s:", header).getBytes(
                        StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        if (lastSuccessfulThreadsRunnerResults != null) {
            Files.write(
                    performanceReport,
                    String.format(
                            "%n    Last successful performance stats:%n%s",
                            print(lastSuccessfulThreadsRunnerResults)).getBytes(
                                    StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        }
        if (unsuccessfulThreadsRunnerResults != null) {
            Files.write(
                    performanceReport,
                    String.format(
                            "%n    Unsuccessful performance stats:%n%s",
                            print(unsuccessfulThreadsRunnerResults)).getBytes(
                                    StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        }
    }

}
