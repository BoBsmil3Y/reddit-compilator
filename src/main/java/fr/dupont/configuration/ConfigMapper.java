package fr.dupont.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.ColorLogger;
import fr.dupont.Main;
import fr.dupont.models.Config;
import fr.dupont.models.Subreddit;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ConfigMapper {

    private final ColorLogger logger = new ColorLogger();

    public void writeConfigFile(Config config) {

        logger.print(ColorLogger.Level.INFO, "Writing in config file...");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(Main.CONFIG_PATH), config);
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to write in config file !\n" + e.getMessage());
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");

    }

    public Config readConfigFile() throws IOException {
        Config config;

        logger.print(ColorLogger.Level.INFO, "Reading config file...");
        try {
            config = new ObjectMapper().readValue(new File(Main.CONFIG_PATH), Config.class);
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to read config file !\n" + e.getMessage());
            throw new IOException("Failed to read config file !");
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");
        return config;
    }

}
