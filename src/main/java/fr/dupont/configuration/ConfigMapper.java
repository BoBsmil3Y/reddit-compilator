package fr.dupont.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.ColorLogger;
import fr.dupont.Main;
import fr.dupont.exceptions.FailedToReadConfig;
import fr.dupont.exceptions.FailedToWriteConfig;
import fr.dupont.models.Config;
import fr.dupont.models.Subreddit;

import java.io.*;
import java.util.List;

public class ConfigMapper {

    private final ColorLogger logger = new ColorLogger();

    public Config getConfig(){
        Config config;
        try {
            config = readConfigFile();
        } catch (FailedToReadConfig e) {
            logger.print(ColorLogger.Level.INFO, "Creating a new one ...");
            config = new Config(1, 1, "video title",
                    List.of(new Subreddit("Unexpected", 15F, 4, 40)),
                    List.of(new Subreddit("Cursed_Images", 0F, 0, 0))
            );
            try {
                writeConfigFile(config);
                logger.print(ColorLogger.Level.WARNING, "The compilator will stop to let you configure the config.json file.");
            } catch (FailedToWriteConfig e1) {
                logger.print(ColorLogger.Level.ERROR, "Fatal error! Stopping compilator.");
            } finally {
                System.exit(1);
            }
        }

        return config;
    }

    private void writeConfigFile(Config config) throws FailedToWriteConfig {

        logger.print(ColorLogger.Level.INFO, "Writing in config file...");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(Main.CONFIG_PATH), config);
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to write in config file !\n" + e.getMessage());
            throw new FailedToWriteConfig();
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");

    }

    private Config readConfigFile() throws FailedToReadConfig {
        Config config;

        logger.print(ColorLogger.Level.INFO, "Reading config file...");
        try {
            config = new ObjectMapper().readValue(new File(Main.CONFIG_PATH), Config.class);
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to read config file !\n" + e.getMessage());
            throw new FailedToReadConfig();
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");
        return config;
    }

}
