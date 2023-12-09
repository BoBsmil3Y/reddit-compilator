package fr.dupont;

import fr.dupont.configuration.ConfigMapper;
import fr.dupont.exceptions.*;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.*;
import fr.dupont.picker.VideoPicker;
import fr.dupont.videomanipulation.MergeMediaFiles;

import java.util.List;

public class Main {
    public static final String OUTPUT_FOLDER = "./output/downloaded/";
    public static final String CONFIG_PATH = "./src/main/resources/config.json";

    private static Config config;
    public static void main(String[] args) {

        ColorLogger logger = new ColorLogger();
        logger.print(ColorLogger.Level.LOG, "Starting >> Reddit compilator project rewritten in Java.");

        FolderUtils.createLocalFile(OUTPUT_FOLDER, false);
        FolderUtils.createLocalFile(CONFIG_PATH, true);

        ConfigMapper cm = new ConfigMapper();
        try {
            config = cm.readConfigFile();
        } catch (FailedToReadConfig e) {
            logger.print(ColorLogger.Level.INFO, "Creating a new one ...");
            config = new Config(1, 1, "video title", List.of(new Subreddit("Unexpected", 15F, 4, 40)));
            try {
                cm.writeConfigFile(config);
                logger.print(ColorLogger.Level.WARNING, "The compilator will stop to let you configure the config.json file.");
            } catch (FailedToWriteConfig e1) {
                logger.print(ColorLogger.Level.ERROR, "Fatal error! Stopping compilator.");
            } finally {
                System.exit(1);
            }
        }

        final MergeMediaFiles merger = new MergeMediaFiles();

        if (! merger.isFfmpegIsInstalled())
            System.exit(1);

        new VideoPicker(config.subreddits()).pickVideos();

        //TODO: Add a selection class to select the best videos
        //TODO: Add a progress bar
        //TODO: Add image download and parser (has to be fix / rewrite to add a better image parser)

    }
}