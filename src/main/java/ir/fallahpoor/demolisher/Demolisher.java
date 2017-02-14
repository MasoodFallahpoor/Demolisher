package ir.fallahpoor.demolisher;

import org.apache.commons.cli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

        final int[] filesDeleted = {0};
        final int[] errorsCount = {0};

        // Traverse the file tree rooted at directoryPath and get all paths
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {

            // Get paths that point to a regular file and file name is one of the provided file names
            paths.filter(filePath -> Files.isRegularFile(filePath) &&
                    fileNames.contains(filePath.toFile().getName()))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {
                        boolean result = deleteFile(filePath);
                        if (result) {
                            filesDeleted[0]++;
                            if (commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_LONG) ||
                                    commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_SHORT)) {
                                System.out.println("File " + filePath.toFile().getAbsolutePath() + " deleted.");
                            }
                        } else {
                            errorsCount[0]++;
                            if (commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_LONG) ||
                                    commandLine.hasOption(DemolisherOptions.OPTION_VERBOSE_SHORT)) {
                                System.out.println("Could NOT delete " + filePath.toFile().getAbsolutePath());
                            }
                        }
                    });

            System.out.println("\n" + filesDeleted[0] + " files deleted, " + errorsCount[0] + " errors occurred.");

        } catch (IOException e) {
            System.err.println("ERROR: Could NOT get the list of files");
            System.exit(1);
        }

    }

    private boolean isDirectoryPathOk(String directoryPath) {

        File dirPath = new File(directoryPath);
        boolean result = true;

        if (!dirPath.exists()) {
            System.out.println(DemolisherOptions.PROGRAM_NAME + ": cannot access '" + dirPath.getAbsolutePath() +
                    "': No such directory");
            result = false;
        }

        if (dirPath.isFile()) {
            System.out.println("No directory is specified");
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

}
