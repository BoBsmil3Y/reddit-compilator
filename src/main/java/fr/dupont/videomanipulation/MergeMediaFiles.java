package fr.dupont.videomanipulation;

import fr.dupont.ColorLogger;
import fr.dupont.exceptions.FailedToMergeVideo;
import fr.dupont.models.Config;
import fr.dupont.models.Video;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Merge media files with FFMPEG.
 */
public class MergeMediaFiles {

    private final ColorLogger logger = new ColorLogger();

    /**
     * Merge a video and an audio file.
     * @param video The video to merge.
     */
    public void mergeAudioAndVideo(Video video) throws FailedToMergeVideo {

        final String localUrlMerged = video.getLocalUrl().replace(".mp4", "-merged.mp4");
        final String[] command = {"ffmpeg", "-i", video.getLocalUrl(), "-i", video.getLocalAudioUrl(), "-c:v", "copy", "-c:a", "aac", "-y", localUrlMerged};

        try {
            final Process process = new ProcessBuilder(command).start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while (reader.readLine() != null); //Read error stream to wait for stream close
            logger.print(ColorLogger.Level.SUCCESS, "Merge successful for : " + video.getUrl() + " !");
        } catch (Exception e) {
            logger.print(ColorLogger.Level.ERROR, "An error occurred when merging :  " + video.getUrl() + " ! Error: \n" + e.getMessage() );
            throw new FailedToMergeVideo();
        }
    }

    /**
     * Concat a list of videos into one final.
     * @param config The config object.
     * @param videos The list of videos to merge.
     */
    public void mergeVideos(Config config, List<Video> videos) {

        final String localUrlMerged = config.video_number() + "-" + config.video_base_name() + ".mp4";
        final ArrayList<String> command = new ArrayList<>();

        command.add("ffmpeg");

        videos.forEach(video -> {
            command.add("-i");
            command.add(video.getLocalUrl());
        });

        command.add("-filter_complex");
        command.add("concat=n=" + videos.size() + ":v=1:a=1");
        command.add("-c:a");
        command.add("aac");
        command.add("-y");
        command.add(localUrlMerged);

        try {
            logger.print(ColorLogger.Level.LOG, "Starting merge for final video ...");
            final Process process = new ProcessBuilder(command).start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println(line); //Read error stream to wait for stream close
            logger.print(ColorLogger.Level.SUCCESS, "Merge successful! ");
        } catch (Exception e) {
            logger.print(ColorLogger.Level.ERROR, "An error occurred when merging the final video!");
        }
    }

    /**
     * Check if FFMPEG is installed.
     * @return true if FFMPEG is installed, false otherwise.
     */
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
