package fr.dupont.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.ColorLogger;
import fr.dupont.Main;
import fr.dupont.exceptions.FailedToReadConfig;
import fr.dupont.exceptions.FailedToWriteConfig;
import fr.dupont.models.Config;

import java.io.*;

public class ConfigMapper {

    private final ColorLogger logger = new ColorLogger();

    public void writeConfigFile(Config config) throws FailedToWriteConfig {

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

    public Config readConfigFile() throws FailedToReadConfig {
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
