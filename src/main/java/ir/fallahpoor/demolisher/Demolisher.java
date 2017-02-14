package ir.fallahpoor.demolisher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Receives a directory and one or more file names and deletes files with those file names from given directory and
 * all its subdirectories.
 *
 * @author Masood Fallahpoor
 * @version 0.1
 */
public class Demolisher {

    private static final int NUM_MANDATORY_ARGUMENTS = 2;

    public static void main(String[] args) {

        if (args.length < NUM_MANDATORY_ARGUMENTS) {
            // Wrong number of arguments. Display the usage of program
            System.out.println("Wrong number of arguments.\nUsage: Demolisher PATH-TO-DIRECTORY FILE-NAME-1 " +
                    "[FILE-NAME-2 FILE-NAME-3 ...]");
        } else {

            String directoryPath = args[0];
            boolean resultOk = checkDirectoryPath(directoryPath);

            if (resultOk) {
                ArrayList<String> fileNames = new ArrayList<>();
                fileNames.addAll(Arrays.asList(args).subList(1, args.length));
                deleteFiles(directoryPath, fileNames);
            } else {
                System.exit(1);
            }

        }

    }

    private static boolean checkDirectoryPath(String directoryPath) {

        File dirPath = new File(directoryPath);
        boolean result = true;

        if (!dirPath.exists()) {
            System.out.println("Specified directory does not exist! Exiting...");
            result = false;
        }

        if (dirPath.isFile()) {
            System.out.println("You specified a file NOT a directory! Exiting...");
            result = false;
        }

        return result;

    }

    private static void deleteFiles(String directoryPath, List<String> fileNames) {

        // Traverse the file tree rooted at directoryPath and get all paths
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {

            // Get paths that point to a regular file and file name is one of the provided file names
            paths.filter(filePath -> Files.isRegularFile(filePath) &&
                    fileNames.contains(filePath.toFile().getName()))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {
                        try {
                            boolean result = Files.deleteIfExists(filePath);
                            if (result) {
                                System.out.println("File " + filePath.toFile().getAbsolutePath() + " deleted.");
                            } else {
                                System.out.println("Could NOT delete " + filePath.toFile().getAbsolutePath());
                            }
                        } catch (IOException e) {
                            System.err.println("ERROR: Could NOT delete file");
                        }
                    });

        } catch (IOException e) {
            System.err.println("ERROR: Could NOT get the list of files");
        }

    }

}
