package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logger for SSL/TLS/DTLS.
 * <p>
 * If the system property
 * {@code com.github.jh3nd3rs0n.jargyle.internal.net.debug} is not set, the
 * debug logging is turned off.  If the system property
 * {@code com.github.jh3nd3rs0n.jargyle.internal.net.debug} is set as empty,
 * the debug logger is specified by
 * {@code System.getLogger("com.github.jh3nd3rs0n.jargyle.internal.net.ssl")},
 * and applications can customize and configure the logger or use external
 * logging mechanisms. If the system property
 * {@code com.github.jh3nd3rs0n.jargyle.internal.net.debug} is set and
 * non-empty, a private debug logger implemented in this class is used.
 * </p>
 */
final class SslLogger {

    private static final String INDENTATION_SPACES = "    ";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final MessageFormat KEY_OBJECT_FORMAT = new MessageFormat(
            "\"{0}\" : '{'" + LINE_SEPARATOR +
                    "{1}" + LINE_SEPARATOR +
                    "'}'" + LINE_SEPARATOR,
            Locale.ENGLISH);

    private final System.Logger logger;
    private final boolean on;
    private final String property;

    public SslLogger() {
        System.Logger l = null;
        boolean o = false;
        String p = System.getProperty("com.github.jh3nd3rs0n.jargyle.internal.net.debug");
        if (p != null) {
            if (p.isEmpty()) {
                l = System.getLogger("com.github.jh3nd3rs0n.jargyle.internal.net.ssl");
            } else {
                if (p.equals("help")) {
                    help();
                }
                l = new SslConsoleLogger("com.github.jh3nd3rs0n.jargyle.internal.net.ssl", p);
            }
            o = true;
        }
        this.logger = l;
        this.on = o;
        this.property = p;
    }

    private static String formatObject(final Object obj) {
        return obj.toString();
    }

    private static String formatParameters(final Object... parameters) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object parameter : parameters) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(",").append(LINE_SEPARATOR);
            }
            if (parameter instanceof Throwable) {
                builder.append(formatThrowable((Throwable) parameter));
            } else {
                builder.append(formatObject(parameter));
            }
        }

        return builder.toString();
    }

    private static String formatThrowable(final Throwable throwable) {
        StringBuilder builder = new StringBuilder();
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(bytesOut)) {
            throwable.printStackTrace(out);
            builder.append(indent(bytesOut.toString()));
        }
        Object[] fields = {
                "throwable",
                builder.toString()
        };
        return KEY_OBJECT_FORMAT.format(fields);
    }

    private static void help() {
        System.err.println();
        System.err.println("help           print the help information");
        System.err.println("expand         expand debugging information");
        System.err.println();
        System.err.println("all            turn on all debugging");
        System.err.println("dtls           turn on dtls debugging");
        System.err.println();
        System.exit(0);
    }

    private static String indent(final String string) {
        Pattern pattern = Pattern.compile("([^\r\n]*)(\r\n|\r|\n|$)");
        Matcher matcher = pattern.matcher(string);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(INDENTATION_SPACES).append(matcher.group(1)).append(matcher.group(2));
        }
        return sb.toString();
    }

    public void debug(
            final String checkPoints,
            final String message,
            final Object... params) {
        this.log(System.Logger.Level.DEBUG, checkPoints, message, params);
    }

    private boolean hasOption(final String option) {
        if (this.property.contains("all")) {
            return true;
        }
        return this.property.contains(option);
    }

    private boolean hasOptions(final String options) {
        String[] opts = options.split(",");
        for (String opt : opts) {
            if (!this.hasOption(opt.trim())) {
                return false;
            }
        }
        return true;
    }

    private void log(
            final System.Logger.Level level,
            final String checkPoints,
            final String message,
            final Object... params) {
        if (!this.on) {
            return;
        }
        if (!this.logger.isLoggable(level)) {
            return;
        }
        if (!this.property.isEmpty() && !this.hasOptions(checkPoints)) {
            return;
        }
        if (params == null || params.length == 0) {
            this.logger.log(level, message);
            return;
        }
        String formatted = formatParameters(params);
        if (this.logger instanceof SslConsoleLogger) {
            this.logger.log(level, message, formatted);
        } else {
            this.logger.log(level, message + ":" + LINE_SEPARATOR + formatted);
        }
    }

    private static class SslConsoleLogger implements System.Logger {

        private static final MessageFormat MESSAGE_FORMAT_NO_PARAS =
                new MessageFormat(
                        "'{'" + LINE_SEPARATOR +
                                " \"logger\"      : \"{0}\"," + LINE_SEPARATOR +
                                " \"level\"       : \"{1}\"," + LINE_SEPARATOR +
                                " \"thread id\"   : \"{2}\"," + LINE_SEPARATOR +
                                " \"thread name\" : \"{3}\"," + LINE_SEPARATOR +
                                " \"time\"        : \"{4}\"," + LINE_SEPARATOR +
                                " \"caller\"      : \"{5}\"," + LINE_SEPARATOR +
                                " \"message\"     : \"{6}\"" + LINE_SEPARATOR +
                                "'}'" + LINE_SEPARATOR,
                        Locale.ENGLISH);

        private static final MessageFormat MESSAGE_COMPACT_FORMAT_NO_PARAS =
                new MessageFormat(
                        "{0}|{1}|{2}|{3}|{4}|{5}|{6}" + LINE_SEPARATOR,
                        Locale.ENGLISH);

        private static final MessageFormat MESSAGE_FORMAT_WITH_PARAS =
                new MessageFormat(
                        "'{'" + LINE_SEPARATOR +
                                " \"logger\"      : \"{0}\"," + LINE_SEPARATOR +
                                " \"level\"       : \"{1}\"," + LINE_SEPARATOR +
                                " \"thread id\"   : \"{2}\"," + LINE_SEPARATOR +
                                " \"thread name\" : \"{3}\"," + LINE_SEPARATOR +
                                " \"time\"        : \"{4}\"," + LINE_SEPARATOR +
                                " \"caller\"      : \"{5}\"," + LINE_SEPARATOR +
                                " \"message\"     : \"{6}\"," + LINE_SEPARATOR +
                                " \"specifics\"   : [" + LINE_SEPARATOR +
                                "{7}" + LINE_SEPARATOR +
                                "  ]" + LINE_SEPARATOR +
                                "'}'" + LINE_SEPARATOR,
                        Locale.ENGLISH);

        private static final MessageFormat MESSAGE_COMPACT_FORMAT_WITH_PARAS =
                new MessageFormat(
                        "{0}|{1}|{2}|{3}|{4}|{5}|{6} (" + LINE_SEPARATOR +
                                "{7}" + LINE_SEPARATOR +
                                ")" + LINE_SEPARATOR,
                        Locale.ENGLISH);

        private static final String PATTERN = "yyyy-MM-dd kk:mm:ss.SSS z";

        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
                        PATTERN, Locale.ENGLISH)
                .withZone(ZoneId.systemDefault());

        private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

        private static final String THREAD_GET_ID_METHOD_NAME = "getId";
        private static final String THREAD_THREAD_ID_METHOD_NAME = "threadId";

        private static boolean initialized = false;
        private static Method threadGetIdMethod = null;
        private static Method threadThreadIdMethod = null;

        private final String loggerName;
        private final boolean useCompactFormat;

        SslConsoleLogger(final String name, final String options) {
            this.loggerName = name;
            this.useCompactFormat = !options.contains("expand");
        }

        private static String formatCaller() {
            return StackWalker.getInstance().walk(s ->
                    s.dropWhile(f ->
                                    f.getClassName().startsWith(SslLogger.class.getName()) ||
                                            f.getClassName().startsWith(System.class.getName()))
                            .map(f -> f.getFileName() + ":" + f.getLineNumber())
                            .findFirst().orElse("unknown caller"));
        }

        private static long getThreadId(final Thread thread) {
            initializeIfNotInitialized();
            if (threadThreadIdMethod != null) {
                try {
                    return (long) threadThreadIdMethod.invoke(thread);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new AssertionError(e);
                }
            }
            try {
                return (long) threadGetIdMethod.invoke(thread);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }

        private static void initializeIfNotInitialized() {
            REENTRANT_LOCK.lock();
            try {
                if (initialized) {
                    return;
                }
                try {
                    threadThreadIdMethod = Thread.class.getMethod(
                            THREAD_THREAD_ID_METHOD_NAME);
                } catch (NoSuchMethodException ignored) {
                }
                if (threadThreadIdMethod == null) {
                    try {
                        threadGetIdMethod = Thread.class.getMethod(
                                THREAD_GET_ID_METHOD_NAME);
                    } catch (NoSuchMethodException ignored) {
                    }
                }
                initialized = true;
            } finally {
                REENTRANT_LOCK.unlock();
            }
        }

        private String format(
                final Level level,
                final String message,
                final Object... parameters) {
            if (parameters == null || parameters.length == 0) {
                Object[] messageFields = {
                        this.loggerName,
                        level.getName(),
                        Long.toHexString(getThreadId(Thread.currentThread())),
                        Thread.currentThread().getName(),
                        DATE_TIME_FORMATTER.format(Instant.now()),
                        formatCaller(),
                        message
                };
                if (this.useCompactFormat) {
                    return MESSAGE_COMPACT_FORMAT_NO_PARAS.format(messageFields);
                } else {
                    return MESSAGE_FORMAT_NO_PARAS.format(messageFields);
                }
            }
            Object[] messageFields = {
                    this.loggerName,
                    level.getName(),
                    Long.toHexString(getThreadId(Thread.currentThread())),
                    Thread.currentThread().getName(),
                    DATE_TIME_FORMATTER.format(Instant.now()),
                    formatCaller(),
                    message,
                    (this.useCompactFormat ?
                            formatParameters(parameters) :
                            indent(formatParameters(parameters)))
            };
            if (this.useCompactFormat) {
                return MESSAGE_COMPACT_FORMAT_WITH_PARAS.format(messageFields);
            } else {
                return MESSAGE_FORMAT_WITH_PARAS.format(messageFields);
            }
        }

        @Override
        public String getName() {
            return this.loggerName;
        }

        @Override
        public boolean isLoggable(final Level level) {
            return level != Level.OFF;
        }

        @Override
        public void log(
                final Level level,
                final ResourceBundle rb,
                final String message,
                final Throwable thrwbl) {
            if (this.isLoggable(level)) {
                try {
                    String formatted = this.format(level, message, thrwbl);
                    System.err.write(formatted.getBytes(StandardCharsets.UTF_8));
                } catch (Exception ignored) {
                }
            }
        }

        @Override
        public void log(
                final Level level,
                final ResourceBundle rb,
                final String message,
                final Object... params) {
            if (this.isLoggable(level)) {
                try {
                    String formatted = this.format(level, message, params);
                    System.err.write(formatted.getBytes(StandardCharsets.UTF_8));
                } catch (Exception ignored) {
                }
            }
        }

    }

}
