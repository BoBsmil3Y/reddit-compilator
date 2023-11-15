package fr.dupont.videomanipulation;

import fr.dupont.models.Video;

public class MergeMediaFiles {

    public void mergeAudioAndVideo(Video video) {

        final String localUrlMerged = video.getLocalUrl().replace(".mp4", "-merged.mp4");
        String[] command = {"ffmpeg", "-i", video.getLocalUrl(), "-i", video.getLocalAudioUrl(), "-c:v", "copy", "-c:a", "aac", localUrlMerged};

        try {
            new ProcessBuilder(command).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
