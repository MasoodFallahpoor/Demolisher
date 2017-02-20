package ir.fallahpoor.demolisher;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author Masood Fallahpoor
 */
public class DemolisherOptions {

    public static final int NUM_MANDATORY_ARGUMENTS = 2;
    public static final String PROGRAM_NAME = "demolisher";
    public static final String PROGRAM_VERSION = "0.4";
    public static final String USAGE_MESSAGE = PROGRAM_NAME + " [OPTION]... DIRECTORY FILE-NAME...";
    public static final String VERSION_MESSAGE = PROGRAM_NAME + " version " + PROGRAM_VERSION + "\n" +
            "Copyright (C) 2017 Masood Fallahpoor.\n" +
            "License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.\n" +
            "This is free software: you are free to change and redistribute it.\n" +
            "There is NO WARRANTY, to the extent permitted by law.\n\n" +
            "Written by Masood Fallahpoor.\n";
    public static final String OPTION_INTERACTIVE_SHORT = "i";
    public static final String OPTION_INTERACTIVE_LONG = "interactive";
    public static final String OPTION_VERBOSE_SHORT = "v";
    public static final String OPTION_VERBOSE_LONG = "verbose";
    public static final String OPTION_VERSION = "version";
    public static final String OPTION_HELP_SHORT = "h";
    public static final String OPTION_HELP_LONG = "help";

    public static Options getOptions() {

        Options options = new Options();

        // Add an option for being more verbose
        Option interactiveOption = new Option(OPTION_INTERACTIVE_SHORT, OPTION_INTERACTIVE_LONG, false,
                "prompt before each deletion.");
        options.addOption(interactiveOption);

        // Add an option for being more verbose
        Option verboseOption = new Option(OPTION_VERBOSE_SHORT, OPTION_VERBOSE_LONG, false,
                "explain what is being done.");
        options.addOption(verboseOption);

        // Add an option for displaying version information
        Option versionOption = new Option(null, OPTION_VERSION, false,
                "output version information and exit.");
        options.addOption(versionOption);

        Option helpOption = new Option(OPTION_HELP_SHORT, OPTION_HELP_LONG, false,
                "display program help and exit.");
        options.addOption(helpOption);

        return options;

    }

}
