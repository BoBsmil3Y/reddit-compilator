package fr.dupont;

import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.MediaFormat;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Video;
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

        RedditRepository redditRepository = new RedditRepository();
        System.out.println(redditRepository.getTopVideoListOfASub(subreddits.get(0)));

        Video video = new Video("test", "test", "https://v.redd.it/rnmzllmjihyb1/DASH_360.mp4?source=fallback", 0, false, new MediaFormat(360, 472, "mp4"), null, 21F, "https://v.redd.it/rnmzllmjihyb1/DASH_audio.mp4?source=fallback");
        Video video2 = new Video("test", "test", "https://v.redd.it/rnmzllmjihyb1/DASH_audio.mp4?source=fallback", 0, false, new MediaFormat(360, 472, "mp4"), null, 21F, "https://v.redd.it/rnmzllmjihyb1/DASH_audio.mp4?source=fallback");
        try {
            redditRepository.getVideo(video);
            redditRepository.getVideo(video2);
        } catch (FailedToRetrievedMedia e) {
            throw new RuntimeException(e);
        }


    }
}