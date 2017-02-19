package ir.fallahpoor.demolisher;

import org.apache.commons.cli.CommandLine;

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

            // Get paths that point to a regular file
            paths.filter(Files::isRegularFile)
                    .filter(filePath -> FileUtils.isFileNameInGivenFileNames(filePath, fileNames))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {

                        boolean isDeleted = FileUtils.deleteFile(filePath);

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
            System.out.println("\n" + deletionResultMap.getOrDefault(true, 0) + " files deleted\n" +
                    deletionResultMap.getOrDefault(false, 0) + " errors occurred");

        } catch (IOException e) {
            showError("Could NOT get the list of files");
            System.exit(1);
        }

    }

    private boolean isDirectoryPathOk(String directoryPath) {

        FileUtils.Result result = FileUtils.isDirectoryPathOk(directoryPath);

        if (result == FileUtils.Result.OK) {
            return true;
        } else {

            if (result == FileUtils.Result.DIR_DOES_NOT_EXIST) {
                showError("directory '" + directoryPath + "' does NOT exist");
            } else if (result == FileUtils.Result.NOT_A_DIRECTORY) {
                showError("A file is specified, NOT a directory");
            }

            return false;

        }

    }

    private void showError(String errorMessage) {
        System.err.println(DemolisherOptions.PROGRAM_NAME + ": " + errorMessage);
    }

}
