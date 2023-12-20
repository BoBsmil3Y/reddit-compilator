package fr.dupont.service;

/**
 * YouTube service to communicate with YouTube API.
 */
public interface YoutubeService {

    void uploadVideo(String videoPath);

    int getVideoLikes(String videoId);

    int getVideoDislikes(String videoId);

    int getVideoViews(String videoId);


}
