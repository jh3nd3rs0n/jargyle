package com.github.jh3nd3rs0n.jargyle.test.echo.server.performance;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ThrowableHelper {

    private ThrowableHelper() {
    }

    public static String toStackTraceString(final Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString().trim();
    }

}
