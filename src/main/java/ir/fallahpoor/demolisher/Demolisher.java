/*
 * Copyright 2017 Masood Fallahpoor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        int depth = Integer.MAX_VALUE;

        if (commandLine.hasOption(DemolisherOptions.OPTION_DEPTH_SHORT)) {

            String depthOptionValue = commandLine.getOptionValue(DemolisherOptions.OPTION_DEPTH_SHORT);

            try {
                depth = Integer.valueOf(depthOptionValue);
            } catch (NumberFormatException ex) {
                showError("invalid argument '" + depthOptionValue + "' for "
                        + DemolisherOptions.OPTION_DEPTH_LONG
                        + "\n"
                        + "depth must be an integer in range [0," + Integer.MAX_VALUE + "]");
                System.exit(1);
            }

        }

        // Traverse the file tree rooted at directoryPath and get all paths
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath), depth)) {

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

            if (deletionResultMap.getOrDefault(DeleteResult.ERROR, 0) == 0) {
                System.exit(0);
            } else {
                System.exit(1);
            }

        } catch (IllegalArgumentException e) {
            showError("invalid argument '" + depth + "' for " + DemolisherOptions.OPTION_DEPTH_LONG +
                    "\ndepth must be an integer in range [0," + Integer.MAX_VALUE + "]");
            System.exit(1);
        } catch (IOException e) {
            showError("Could NOT get the list of files");
            System.exit(1);
        }

    }

    private boolean isDirectoryPathOk(String directoryPath) {

        FileUtils.DirectoryCheckResult directoryCheckResult = FileUtils.isDirectoryPathOk(directoryPath);

        if (directoryCheckResult == FileUtils.DirectoryCheckResult.OK) {
            return true;
        } else {

            if (directoryCheckResult == FileUtils.DirectoryCheckResult.DIR_DOES_NOT_EXIST) {
                showError("directory '" + directoryPath + "' does NOT exist");
            } else if (directoryCheckResult == FileUtils.DirectoryCheckResult.NOT_A_DIRECTORY) {
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
