package com.github.jh3nd3rs0n.jargyle.cli;

import java.io.*;

final class PseudoConsole extends AbstractConsole {

    public PseudoConsole() {
    }

    @Override
    public void printf(final String format, final Object... args) {
        System.out.printf(format, args);
    }

    @Override
    public String readLine(final String fmt, final Object... args) {
        this.printf(fmt, args);
        String line;
        SwappableInputStream swapInputStream = new SwappableInputStream(
                System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                swapInputStream));
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new IOError(e);
        } finally {
            swapInputStream.setInputStream(new NullInputStream());
            try {
                reader.close();
            } catch (IOException e) {
                throw new IOError(e);
            }
        }
        return line;
    }

    @Override
    public char[] readPassword(final String fmt, final Object... args) {
        return this.readLine(fmt, args).toCharArray();
    }

    private static final class NullInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            return -1;
        }

    }

    private static final class SwappableInputStream extends FilterInputStream {

        public SwappableInputStream(final InputStream i) {
            super(i);
        }

        public void setInputStream(final InputStream i) {
            this.in = i;
        }

    }

}
