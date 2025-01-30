package com.github.jh3nd3rs0n.jargyle.cli;

import com.github.jh3nd3rs0n.jargyle.argmatey.ArgMatey;

import java.io.File;
import java.io.PrintWriter;

public final class ReferenceDocsGeneratorCLI extends ArgMatey.CLI {

    private static final int DESTINATION_DIRECTORY_OPTION_GROUP_ORDINAL = 0;
    private static final int HELP_OPTION_GROUP_ORDINAL = 1;

    private File destinationDirectory;
    private final String programBeginningUsage;
    private final String suggestion;

    public ReferenceDocsGeneratorCLI(
            final String progName,
            final String progBeginningUsage,
            final String[] args,
            final boolean posixlyCorrect) {
        super(args, posixlyCorrect);
        String prgName = progName;
        if (prgName == null) {
            prgName = this.getClass().getName();
        }
        String prgBeginningUsage = progBeginningUsage;
        if (prgBeginningUsage == null) {
            prgBeginningUsage = prgName;
        }
        ArgMatey.Option helpOption = this.getOptionGroups().get(
                HELP_OPTION_GROUP_ORDINAL).get(0);
        String suggest = String.format(
                "Try `%s %s' for more information",
                prgBeginningUsage,
                helpOption.getUsage());
        this.destinationDirectory = null;
        this.programBeginningUsage = prgBeginningUsage;
        this.suggestion = suggest;
        this.setProgramName(prgName);
    }

    public static void main(final String[] args) {
        ArgMatey.CLI cli = new ReferenceDocsGeneratorCLI(
                null, null, args, false);
        try {
            cli.handleArgs();
        } catch (ArgMatey.TerminationRequestedException e) {
            System.exit(e.getExitStatusCode());
        }
    }

    @Override
    protected void afterHandleArgs() throws ArgMatey.TerminationRequestedException {
        try {
            new ReferenceDocsGenerator().generateReferenceDocs(
                    this.destinationDirectory);
        } catch (Throwable t) {
            System.err.printf("%s: %s%n", this.getProgramName(), t);
            t.printStackTrace(System.err);
            throw new ArgMatey.TerminationRequestedException(-1);
        }
        throw new ArgMatey.TerminationRequestedException(0);
    }

    @Override
    protected void beforeHandleArgs() {
        this.destinationDirectory = null;
    }

    @ArgMatey.Annotations.Option(
            doc = "Print this help and exit",
            name = "help",
            type = ArgMatey.OptionType.GNU_LONG
    )
    @ArgMatey.Annotations.Option(
            name = "h",
            type = ArgMatey.OptionType.POSIX
    )
    @ArgMatey.Annotations.Ordinal(HELP_OPTION_GROUP_ORDINAL)
    @Override
    protected void displayProgramHelp() throws ArgMatey.TerminationRequestedException {
        this.printProgramHelp(new PrintWriter(System.out, true));
        throw new ArgMatey.TerminationRequestedException(0);
    }

    @Override
    protected void displayProgramVersion() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    protected void handleNonparsedArg(
            final String nonparsedArg) throws ArgMatey.TerminationRequestedException {
        throw new IllegalArgumentException(String.format(
                "extra operand `%s'", nonparsedArg));
    }

    @Override
    protected void handleThrowable(final Throwable t)
            throws ArgMatey.TerminationRequestedException {
        System.err.printf("%s: %s%n", this.getProgramName(), t);
        System.err.println(this.suggestion);
        t.printStackTrace(System.err);
        throw new ArgMatey.TerminationRequestedException(-1);
    }

    void printProgramHelp(final PrintWriter pw) {
        ArgMatey.Option destinationDirectoryOption =
                this.getOptionGroups().get(
                        DESTINATION_DIRECTORY_OPTION_GROUP_ORDINAL).get(0);
        ArgMatey.Option helpOption = this.getOptionGroups().get(
                HELP_OPTION_GROUP_ORDINAL).get(0);
        pw.printf("Usage: %s [%s]%n",
                this.programBeginningUsage,
                destinationDirectoryOption.getUsage());
        pw.printf("       %s %s%n",
                this.programBeginningUsage,
                helpOption.getUsage());
        pw.println();
        pw.println("OPTIONS:");
        this.getOptionGroups().printHelpText(pw);
        pw.println();
    }

    @ArgMatey.Annotations.Option(
            doc = "The destination directory for the output files",
            name = "d",
            optionArgSpec = @ArgMatey.Annotations.OptionArgSpec(name = "DIRECTORY"),
            type = ArgMatey.OptionType.POSIX
    )
    @ArgMatey.Annotations.Ordinal(DESTINATION_DIRECTORY_OPTION_GROUP_ORDINAL)
    public void setDestinationDirectory(final String directory) {
        if (this.destinationDirectory != null) {
            throw new IllegalStateException(
                    "the destination directory was already specified");
        }
        File d = new File(directory);
        if (!d.exists()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' not found",
                    d));
        }
        if (!d.isDirectory()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' not a directory",
                    d));
        }
        this.destinationDirectory = d;
    }

}
