package fr.dupont.models;

import lombok.Getter;

@Getter
public class Video extends Media {

    private final Float duration;
    private final String audioUrl;

    public Video(String title, String author, String url, int createdEpochTime, boolean over18, MediaFormat format, Grade grade, Float duration, String audioUrl) {
        super(title, author, url, createdEpochTime, over18, format, grade);
        this.duration = duration;
        this.audioUrl = audioUrl;
    }
}
