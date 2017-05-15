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

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * @author Masood Fallhpoor
 */
public class FileUtils {

    public enum DirectoryCheckResult {DIR_DOES_NOT_EXIST, NOT_A_DIRECTORY, OK}

    public enum DeleteResult {SKIPPED, DELETED, ERROR}

    /**
     * Checks if the file name of file path is in given list of file names.
     *
     * @param filePath       the path of file to check its name
     * @param givenFileNames list of file names
     * @return {@code true} if file name is in the list of given file names
     */
    public static boolean isFileNameInGivenFileNames(Path filePath, List<String> givenFileNames) {

        Iterator<String> iterator = givenFileNames.iterator();
        String fileNameWithExtension = filePath.toFile().getName();
        String fileNameWithoutExtension = FilenameUtils.removeExtension(filePath.toFile().getName());
        boolean result = false;
        boolean done = false;

        while (iterator.hasNext() && !done) {

            String currentGivenFileName = iterator.next();
            String fileName;

            if (FilenameUtils.getExtension(currentGivenFileName).isEmpty()) {
                // Given file name has no extension so get the extension-less file name of filePath
                fileName = fileNameWithoutExtension;
            } else {
                // Given file name has an extension so get the exact file name of filePath
                fileName = fileNameWithExtension;
            }

            if (currentGivenFileName.equalsIgnoreCase(fileName)) {
                result = done = true;
            }

        }

        return result;

    }

    /**
     * Checks whether given directory path exists and is actually a directory (not a file).
     *
     * @param directoryPath path of directory to check
     * @return {@code true} if given path is a valid directory
     */
    public static DirectoryCheckResult isDirectoryPathOk(String directoryPath) {

        File dirPath = new File(directoryPath);

        if (!dirPath.exists()) {
            return DirectoryCheckResult.DIR_DOES_NOT_EXIST;
        }

        if (!dirPath.isDirectory()) {
            return DirectoryCheckResult.NOT_A_DIRECTORY;
        }

        return DirectoryCheckResult.OK;

    }

    /**
     * Deletes the specified file from file system.
     *
     * @param filePath path of file to delete
     * @param prompt   if {@code true} displays a confirmation before deleting file
     * @return {@code true} if file is successfully deleted, {@code false} otherwise
     */
    public static DeleteResult deleteFile(Path filePath, boolean prompt) {

        if (prompt) {

            System.out.print("Delete file '" + filePath.toFile().getAbsolutePath() + "'?(y/n) ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.length() == 1 && input.equalsIgnoreCase("y")) {
                return deleteFile(filePath);
            } else {
                return DeleteResult.SKIPPED;
            }

        } else {
            return deleteFile(filePath);
        }

    }

    private static DeleteResult deleteFile(Path filePath) {
        try {
            return (Files.deleteIfExists(filePath) ? DeleteResult.DELETED : DeleteResult.ERROR);
        } catch (IOException e) {
            return DeleteResult.ERROR;
        }
    }

}
