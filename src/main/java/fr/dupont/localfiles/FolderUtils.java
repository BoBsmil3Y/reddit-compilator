package fr.dupont.localfiles;

import fr.dupont.ColorLogger;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FolderUtils {

    /**
     * Create a folder if it doesn't exist.
     * @param itemPath The path of the folder to create.
     * @param isFile Distinction between file and folder.
     */
    public static void createLocalFile(String itemPath, boolean isFile) {

        ColorLogger logger = new ColorLogger();

        try {

            Path path = Paths.get(itemPath);

            if (Files.exists(path)) {
                logger.print(ColorLogger.Level.INFO, String.format("Directory %s was found.", itemPath));
                return;
            }

            Files.createDirectories(path.getParent());

            logger.print(ColorLogger.Level.INFO, String.format("Item %s not found, creating it ...", itemPath));
            if (isFile)
                Files.createFile(path);
            else
                Files.createDirectory(path);

            logger.print(ColorLogger.Level.SUCCESS, String.format("Item %s created!", itemPath));

        } catch (IOException e) {

            logger.print(ColorLogger.Level.ERROR, String.format("Failed to create %s !%nShutting down the program.%n%s", itemPath, e.getMessage()));
            System.exit(1);

        }

    }

    /**
     * Delete a file.
     * @param path The path of the file to delete.
     */
    public static void deleteFile(String path){
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            new ColorLogger().print(ColorLogger.Level.ERROR, String.format("Failed to delete %s !%n%s", path, e.getMessage()));
        }
    }

}
