package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.Console;

final class SystemConsole extends AbstractConsole {

    private final Console console;

    public SystemConsole(final Console cons) {
        this.console = cons;
    }

    @Override
    public void printf(final String format, final Object... args) {
        this.console.printf(format, args);
    }

    @Override
    public String readLine(final String fmt, final Object... args) {
        return this.console.readLine(fmt, args);
    }

    @Override
    public char[] readPassword(final String fmt, final Object... args) {
        return this.console.readPassword(fmt, args);
    }

}
