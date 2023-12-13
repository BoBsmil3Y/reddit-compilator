package fr.dupont.videomanipulation;

import fr.dupont.ColorLogger;
import fr.dupont.models.Video;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

public class MergeMediaFiles {

    private final ColorLogger logger = new ColorLogger();

    public void mergeAudioAndVideo(Video video) {

        final String localUrlMerged = video.getLocalUrl().replace(".mp4", "-merged.mp4");
        final String[] command = {"ffmpeg", "-i", video.getLocalUrl(), "-i", video.getLocalAudioUrl(), "-c:v", "copy", "-c:a", "aac", "-y", localUrlMerged};

        try {
            final Process process = new ProcessBuilder(command).start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while (reader.readLine() != null); //Read error stream to wait for stream close
            logger.print(ColorLogger.Level.SUCCESS, "Merge successful for : " + video.getUrl() + " !");
        } catch (Exception e) {
            logger.print(ColorLogger.Level.ERROR, "An error occurred when merging :  " + video.getUrl() + " ! Error: \n" + e.getMessage() );
        }
    }

    public boolean isFfmpegIsInstalled() {
        final String[] command = {"ffmpeg", "-version"};
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
