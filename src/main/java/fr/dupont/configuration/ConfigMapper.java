package fr.dupont.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.ColorLogger;
import fr.dupont.models.Subreddit;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigMapper {

    private final ColorLogger logger = new ColorLogger();
    private final String configPath = "./config.json";
    private final String[] params = { "videoNumber", "videoDuration", "videoName" };
    final ArrayList<Subreddit> subreddits = new ArrayList<>(
            Arrays.asList(
                        new Subreddit("Cursed_Images", 15F, 4, 40),
                        new Subreddit("197", 15F, 10, 50),
                        new Subreddit("MoldyMemes", 5F, 8, 25),

                        new Subreddit("Unexpected", 15F, 4, 40),
                        new Subreddit("AbruptChaos", 15F, 10, 50),
                        new Subreddit("StoppedWorking", 5F, 8, 25),
                        new Subreddit("AAAAAAAAAAAAAAAAA", 5F, 2, 20),
                        new Subreddit("WhyWereTheyFilming", 5F, 5, 50),
                        new Subreddit("Whatcouldgowrong", 25F, 4, 40),
                        new Subreddit("perfectlycutscreams", 20F, 3, 30),
                        new Subreddit("funny", 10F, 5, 30)
            )
    );

    public void writeConfigFile() {

        logger.print(ColorLogger.Level.INFO, "Writing in config file...");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(configPath), subreddits);
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to write in config file !\n" + e.getMessage());
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");

    }

    public List<Subreddit> readConfigFile() throws IOException {
        List<Subreddit> subreddits;

        logger.print(ColorLogger.Level.INFO, "Reading config file...");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            subreddits =  objectMapper.readValue(new File(configPath), new TypeReference<List<Subreddit>>() {});
        } catch (IOException e) {
            logger.print(ColorLogger.Level.ERROR, "Failed to read config file !\n" + e.getMessage());
            throw new IOException("Failed to read config file !");
        }
        logger.print(ColorLogger.Level.SUCCESS, "Done !");
        return subreddits;
    }

}
