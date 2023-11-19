package fr.dupont;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.binder.RedditBinder;
import fr.dupont.configuration.ConfigMapper;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.*;
import fr.dupont.repositories.RedditRepository;
import fr.dupont.videomanipulation.MergeMediaFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static final String OUTPUT_FOLDER = "./output/downloaded/";
    public static final String FULL_OUTPUT_FOLDER = "E:/Projets/Reddit-Compilator-java/output/downloaded/";
    public static final String CONFIG_PATH = "./src/main/resources/config.json";

    public static void main(String[] args) throws IOException {

        ColorLogger logger = new ColorLogger();
        logger.print(ColorLogger.Level.LOG, "Starting >> Reddit compilator project rewritten in Java.");

        FolderUtils.createLocalFile(OUTPUT_FOLDER, false);
        FolderUtils.createLocalFile(CONFIG_PATH, true);

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

//        final RedditRepository redditRepository = new RedditRepository();
//        final RedditBinder redditBinder = new RedditBinder();
//        final MergeMediaFiles merger = new MergeMediaFiles();
//
//        if (merger.isFfmpegIsInstalled()){
//            logger.print(ColorLogger.Level.INFO, "FFMPEG is installed !");
//        } else {
//            logger.print(ColorLogger.Level.ERROR, "FFMPEG is not installed !");
//            return;
//        }
//
//        subreddits.forEach(subreddit -> {
//
//            ArrayList<Media> medias = null;
//
//            try {
//                medias = (ArrayList<Media>) redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
//            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
//                e.printStackTrace();
//            }
//
//            medias.forEach(media -> {
//                try {
//                    redditRepository.downloadMedia(media);
//
//                    if (!(media instanceof Video)) return;
//
//                    merger.mergeAudioAndVideo((Video) media);
//
//                } catch (FailedToRetrievedMedia e) {
//                    e.printStackTrace();
//                    FolderUtils.deleteFile(media.getLocalUrl());
//                }
//            });
//        });

        Config config = new Config(10, 10, "video title ", subreddits);
        new ConfigMapper().writeConfigFile(config);

        //TODO: Add a selection class to select the best videos
        //TODO: Add a progress bar
        //TODO: Manage logs on new method
        //TODO: Add image download and parser (has to be fix / rewrite to add a better image parser)

    }
}