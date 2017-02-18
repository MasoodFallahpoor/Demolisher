package ir.fallahpoor.demolisher;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

/**
 * @author Masood Fallhpoor
 */
public class FileUtils {

    public enum Result {DIR_DOES_NOT_EXIST, NOT_A_DIRECTORY, OK}

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

    public static Result isDirectoryPathOk(String directoryPath) {

        File dirPath = new File(directoryPath);

        if (!dirPath.exists()) {
            return Result.DIR_DOES_NOT_EXIST;
        }

        if (!dirPath.isDirectory()) {
            return Result.NOT_A_DIRECTORY;
        }

        return Result.OK;

    }


    public static boolean deleteFile(Path filePath) {

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }

    }

}
