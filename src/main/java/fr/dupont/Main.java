package fr.dupont;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.binder.RedditBinder;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.*;
import fr.dupont.repositories.RedditRepository;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static final String OUTPUT_FOLDER = "./output/downloaded/";
    private static final String CONFIG_PATH = "./src/main/resources/config.yaml";

    public static void main(String[] args) {

        ColorLogger logger = new ColorLogger();
        logger.print(ColorLogger.Level.LOG, "Starting >> Reddit compilator project rewritten in Java.");

        FolderUtils.createLocalFile(OUTPUT_FOLDER, false);
        FolderUtils.createLocalFile(CONFIG_PATH, true);

        //Temporary solution (will be replaced by a config file)
        final ArrayList<Subreddit> subreddits = new ArrayList<>(
                Arrays.asList(
                        new Subreddit("Cursed_Images", 15F, 4, 40),
                        new Subreddit("197", 15F, 10, 50),
                        new Subreddit("MoldyMemes", 5F, 8, 25)

//                        new Subreddit("Unexpected", 15F, 4, 40),
//                        new Subreddit("AbruptChaos", 15F, 10, 50),
//                        new Subreddit("StoppedWorking", 5F, 8, 25),
//                        new Subreddit("AAAAAAAAAAAAAAAAA", 5F, 2, 20),
//                        new Subreddit("WhyWereTheyFilming", 5F, 5, 50),
//                        new Subreddit("Whatcouldgowrong", 25F, 4, 40),
//                        new Subreddit("perfectlycutscreams", 20F, 3, 30),
//                        new Subreddit("funny", 10F, 5, 30)
                )
        );

        RedditRepository redditRepository = new RedditRepository();
        RedditBinder redditBinder = new RedditBinder();

        subreddits.forEach(subreddit -> {

            ArrayList<Media> medias = null;

            try {
                medias = (ArrayList<Media>) redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
                e.printStackTrace();
            }

            medias.forEach(media -> {
                try {
                    redditRepository.downloadMedia(media);
                } catch (FailedToRetrievedMedia e) {
                    e.printStackTrace();
                }
            });
        });


        //TODO: Merge video and audio files
        //TODO: Add a selection class to select the best videos
        //TODO: Add a progress bar
        //TODO: Manage config file
        //TODO: Manage logs on new method
        //TODO: Add image download and parser (has to be fix)


    }
}