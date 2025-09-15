package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.Console;

abstract class AbstractConsole {

    public static AbstractConsole newInstance() {
        Console console = System.console();
        if (console == null) {
            return new PseudoConsole();
        }
        return new SystemConsole(console);
    }

    public abstract void printf(final String format, final Object... args);

    public abstract String readLine(final String fmt, final Object... args);

    public abstract char[] readPassword(final String fmt, final Object... args);

}
