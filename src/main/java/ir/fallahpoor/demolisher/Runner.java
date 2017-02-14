package ir.fallahpoor.demolisher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Masood Fallahpoor
 */
public class Runner {

    private static final int NUM_MANDATORY_ARGUMENTS = 2;

    public static void main(String[] args) {

        if (args.length < NUM_MANDATORY_ARGUMENTS) {
            // Wrong number of arguments. Display the usage of program
            System.out.println("Wrong number of arguments.\n" +
                    "Usage: demolisher PATH-TO-DIRECTORY FILE-NAME-1 [FILE-NAME-2 FILE-NAME-3 ...]");
        } else {

            String directoryPath = args[0];
            ArrayList<String> fileNames = new ArrayList<>();
            fileNames.addAll(Arrays.asList(args).subList(1, args.length));

            Demolisher demolisher = new Demolisher(directoryPath, fileNames);
            demolisher.demolish();

        }

    }

}
