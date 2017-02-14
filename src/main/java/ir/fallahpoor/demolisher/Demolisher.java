package ir.fallahpoor.demolisher;

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

    public Demolisher(String directoryPath, List<String> fileNames) {
        this.directoryPath = directoryPath;
        this.fileNames = fileNames;
    }

    public void demolish() {

        if (!isDirectoryPathOk(directoryPath)) {
            System.exit(1);
        }

        // Traverse the file tree rooted at directoryPath and get all paths
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {

            // Get paths that point to a regular file and file name is one of the provided file names
            paths.filter(filePath -> Files.isRegularFile(filePath) &&
                    fileNames.contains(filePath.toFile().getName()))
                    // Now what we are left with are just those files to be deleted
                    .forEach(filePath -> {
                        boolean result = deleteFile(filePath);
                        if (result) {
                            System.out.println("File " + filePath.toFile().getAbsolutePath() + " deleted.");
                        } else {
                            System.out.println("Could NOT delete " + filePath.toFile().getAbsolutePath());
                        }
                    });

        } catch (IOException e) {
            System.err.println("ERROR: Could NOT get the list of files");
        }

    }

    private boolean isDirectoryPathOk(String directoryPath) {

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


    private boolean deleteFile(Path filePath) {

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }

    }

}
