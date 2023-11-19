package fr.dupont.videomanipulation;

import fr.dupont.ColorLogger;
import fr.dupont.models.Video;

public class MergeMediaFiles {

    private ColorLogger logger = new ColorLogger();

    public void mergeAudioAndVideo(Video video) {

        final String localUrlMerged = video.getLocalUrl().replace(".mp4", "-merged.mp4");
        String[] command = {"ffmpeg", "-i", video.getLocalUrl(), "-i", video.getLocalAudioUrl(), "-c:v", "copy", "-c:a", "aac", localUrlMerged};

        try {
            new ProcessBuilder(command).start();
            logger.print(ColorLogger.Level.SUCCESS, "Merge successful for : " + video.getUrl() + " !");
        } catch (Exception e) {
            logger.print(ColorLogger.Level.ERROR, "An error occurred when merging :  " + video.getUrl() + " ! Error: \n" + e.getMessage() );
        }
    }

    public boolean isFfmpegIsInstalled() {
        String[] command = {"ffmpeg", "-version"};
        try {
            new ProcessBuilder(command).start();
            logger.print(ColorLogger.Level.INFO, "FFMPEG is installed !");
            return true;
        } catch (Exception e) {
            logger.print(ColorLogger.Level.ERROR, "FFMPEG is not installed !");
            return false;
        }
    }

}
