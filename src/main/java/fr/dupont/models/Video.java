package fr.dupont.models;

import lombok.Getter;

@Getter
public class Video extends Media {

    private final Float duration;

    public Video(String title, String author, String url, String permaLink, int createdEpochTime, boolean over18, int height, int width, int score, int ups, Float upvoteRatio, Float duration) {
        super(title, author, url, permaLink, createdEpochTime, over18, height, width, score, ups, upvoteRatio);
        this.duration = duration;
    }
}
