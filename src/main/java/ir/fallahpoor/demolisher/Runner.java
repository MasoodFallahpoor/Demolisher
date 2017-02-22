/*
 *
 *     Copyright (C) 2017 Masood Fallahpoor
 *     This file is part of demolisher.
 *     demolisher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     demolisher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *     You should have received a copy of the GNU General Public License
 *     along with demolisher. If not, see <http://www.gnu.org/licenses/>.
 *
 */

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

        formatter.setWidth(100);
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
                formatter.printHelp(DemolisherOptions.USAGE_MESSAGE, null, options, "\nExit status"
                        + "\n 0 if everything is OK"
                        + "\n 1 if any error occurs (e.g. unrecognized option, non-existing directory, "
                        + "file deletion failure etc)");
                System.exit(0);
            }

        } catch (ParseException e) {
            if (e instanceof UnrecognizedOptionException) {
                System.err.println(DemolisherOptions.PROGRAM_NAME + ": unrecognized option\n" +
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

} // end of class Runner
