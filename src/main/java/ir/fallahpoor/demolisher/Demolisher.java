package ir.fallahpoor.demolisher;

import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * @author Masood Fallahpoor
 */
public class Demolisher {

    private String directoryPath;
    private List<String> fileNames;
    private CommandLine commandLine;

    public Demolisher(String directoryPath, List<String> fileNames, CommandLine commandLine) {
        this.directoryPath = directoryPath;
        this.fileNames = fileNames;
        this.commandLine = commandLine;
    }

    public void demolish() {

        if (!isDirectoryPathOk(directoryPath)) {
            System.exit(1);
        }

        // Traverse the file tree rooted at directoryPath and get all paths
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {

            TreeMap<Boolean, Integer> deletionResultMap = new TreeMap<>();

            // Get paths that point to a regular file and file name is one of the provided file names
            paths.filter(filePath -> Files.isRegularFile(filePath) &&
                    fileNames.contains(filePath.toFile().getName()))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {

                        boolean isDeleted = deleteFile(filePath);

                        // Record whether deletion was successful or not
                        deletionResultMap.put(isDeleted, deletionResultMap.getOrDefault(isDeleted, 0) + 1);

                        // Display a message if verbose option is present
                        if (commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_SHORT)) {
                            if (isDeleted) {
                                System.out.println("File " + filePath.toFile().getAbsolutePath() + " deleted");
                            } else {
                                System.out.println("Could NOT delete " + filePath.toFile().getAbsolutePath());
                            }
                        }

                    });

            // Display number of deleted files and errors
            System.out.println("\n" + deletionResultMap.getOrDefault(true, 0) + " files deleted, " +
                    deletionResultMap.getOrDefault(false, 0) + " errors occurred.");

        } catch (IOException e) {
            showError("Could NOT get the list of files");
            System.exit(1);
        }

    }

    private boolean isDirectoryPathOk(String directoryPath) {

        File dirPath = new File(directoryPath);
        boolean result = true;

        if (!dirPath.exists()) {
            showError("directory '" + dirPath.getAbsolutePath() + "' does NOT exist");
            result = false;
        }

        if (dirPath.isFile()) {
            showError("A file is specified, NOT a directory");
            result = false;
        }

        return result;

    }


    private boolean deleteFile(Path filePath) {

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }

    }

    private void showError(String errorMessage) {
        System.err.println(DemolisherOptions.PROGRAM_NAME + ": " + errorMessage);
    }

}
