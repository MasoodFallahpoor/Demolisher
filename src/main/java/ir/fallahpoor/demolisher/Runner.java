package ir.fallahpoor.demolisher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Masood Fallahpoor
 */
public class Runner {

    public static void main(String[] args) {

        CommandLine commandLine = null;
        Options options = DemolisherOptions.getOptions();
        HelpFormatter formatter = new HelpFormatter();

        // Format options in the order they're added to options object
        formatter.setOptionComparator(null);

        try {
            commandLine = new DefaultParser().parse(options, args);

            /*
            TODO: if user enters both version and help options, no matter which one is input
            first, version option always gets precedence. Devise a way to honor the order they are entered.
             */
            if (commandLine.hasOption(DemolisherOptions.OPTION_VERSION)) {
                System.out.println(DemolisherOptions.VERSION_MESSAGE);
                System.exit(0);
            }

            if (commandLine.hasOption(DemolisherOptions.OPTION_HELP_SHORT)) {
                formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, options);
                System.exit(0);
            }

        } catch (ParseException e) {
            if (e instanceof UnrecognizedOptionException) {
                System.out.println(DemolisherOptions.PROGRAM_NAME + ": unrecognized option\n" +
                        "Try '" + DemolisherOptions.PROGRAM_NAME + " --" + DemolisherOptions.OPTION_HELP_LONG +
                        "' for more information.");
            } else {
                formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, options);
            }
            System.exit(1);
        }

        List<String> argumentsList = commandLine.getArgList();

        if (argumentsList.size() < DemolisherOptions.NUM_MANDATORY_ARGUMENTS) {
            formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, options);
            System.exit(1);
        } else {
            String directoryPath = argumentsList.get(0);
            ArrayList<String> fileNames = new ArrayList<>();
            fileNames.addAll(commandLine.getArgList().subList(1, argumentsList.size()));

            Demolisher demolisher = new Demolisher(directoryPath, fileNames, commandLine);
            demolisher.demolish();
        }

    } // end of method main

} //end of class Runner
