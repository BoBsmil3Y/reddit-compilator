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
     * @param folderPath The path of the folder to create.
     */
    public static void createFolder(String folderPath) {

        ColorLogger logger = new ColorLogger();

        try {

            Path path = Paths.get(folderPath);
            if (Files.exists(path))
                return;

            Files.createDirectories(path);
            logger.print(ColorLogger.Level.SUCCESS, "Directory is created!");

        } catch (IOException e) {

            logger.print(ColorLogger.Level.ERROR,"Failed to create directory! \nShutting down the program.\n" + e.getMessage() );
            System.exit(1);

        }

        logger.print(ColorLogger.Level.INFO, "Directory " + folderPath +  " already exists.");

    }

}
