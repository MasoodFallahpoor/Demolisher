package ir.fallahpoor.demolisher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

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

            if (commandLine.hasOption(DemolisherOptions.OPTION_VERSION)) {
                System.out.println(DemolisherOptions.VERSION_MESSAGE);
                System.exit(0);
            }

            if (commandLine.hasOption(DemolisherOptions.OPTION_HELP_SHORT) ||
                    commandLine.hasOption(DemolisherOptions.OPTION_HELP_LONG)) {
                formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, options);
                System.exit(0);
            }

        } catch (ParseException e) {
            formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, options);
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
