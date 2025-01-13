package fr.dupont;

import fr.dupont.configuration.ConfigMapper;
import fr.dupont.exceptions.*;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.*;
import fr.dupont.picker.ThumbnailPicker;
import fr.dupont.picker.VideoPicker;
import fr.dupont.videomanipulation.MergeMediaFiles;

import java.util.ArrayList;
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
        config = cm.getConfig();

        final MergeMediaFiles merger = new MergeMediaFiles();

        if (! merger.isFfmpegIsInstalled())
            System.exit(1);

        ArrayList<Video> downloadedVideos = (ArrayList<Video>) new VideoPicker(config.video_duration(), config.video_subreddits()).pickVideos();
        new ThumbnailPicker(config.thumbnail_subreddits()).pickThumbnails();

        //TODO: Add a selection class to select the best videos
        //TODO: Add a progress bar
        //TODO: Add image download and parser (has to be fix / rewrite to add a better image parser)

    }
}