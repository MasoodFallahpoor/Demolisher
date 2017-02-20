package ir.fallahpoor.demolisher;

import ir.fallahpoor.demolisher.FileUtils.DeleteResult;
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

            TreeMap<DeleteResult, Integer> deletionResultMap = new TreeMap<>();

            // Get paths that point to a regular file
            paths.filter(Files::isRegularFile)
                    // Get paths to files whose file name is in given list of file names
                    .filter(filePath -> FileUtils.isFileNameInGivenFileNames(filePath, fileNames))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {

                        DeleteResult deleteResult = FileUtils.deleteFile(filePath,
                                commandLine.hasOption(DemolisherOptions.OPTION_INTERACTIVE_SHORT));

                        // Record whether deletion was successful or not
                        deletionResultMap.put(deleteResult, deletionResultMap.getOrDefault(deleteResult, 0)
                                + 1);

                        // Display a message if verbose option is present
                        if (commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_SHORT)) {
                            displayDeletionMessage(deleteResult, filePath.toFile().getAbsolutePath());
                        }

                    });

            displaySummary(deletionResultMap);

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

    private void displayDeletionMessage(DeleteResult result, String path) {

        switch (result) {
            case DELETED:
                System.out.println("File '" + path + "' deleted");
                break;
            case SKIPPED:
                System.out.println("File '" + path + "' skipped");
                break;
            case ERROR:
                System.out.println("Could NOT delete '" + path + "'");
                break;
        }

    }

    private void displaySummary(TreeMap<DeleteResult, Integer> deletionResultMap) {

        System.out.println("\n"
                + deletionResultMap.getOrDefault(DeleteResult.DELETED, 0) + " files deleted"
                + "\n"
                + deletionResultMap.getOrDefault(DeleteResult.SKIPPED, 0) + " files skipped"
                + "\n"
                + deletionResultMap.getOrDefault(DeleteResult.ERROR, 0) + " errors occurred"
        );

    }

}
