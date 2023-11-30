package fr.dupont.models;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Video extends Media {

    private final Float duration;
    private final MediaFormat format;
    private final String localAudioUrl;

    public Video(String title, String author, String url, String localUrl, String localAudioUrl, LocalDateTime createdEpochTime, boolean over18, MediaFormat format, Grade grade, Float duration, Subreddit subreddit) {
        super(title, author, url, localUrl, createdEpochTime, over18, grade, subreddit);
        this.duration = duration;
        this.format = format;
        this.localAudioUrl = localAudioUrl;
    }
    
}
