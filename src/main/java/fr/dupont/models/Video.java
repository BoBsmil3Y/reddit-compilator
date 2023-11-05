package fr.dupont.models;

import lombok.Getter;

@Getter
public class Video extends Media {

    private final Float duration;

    public Video(String title, String author, String url, String permaLink, int createdEpochTime, boolean over18, MediaFormat format, Grade grade, Float duration) {
        super(title, author, url, permaLink, createdEpochTime, over18, format, grade);
        this.duration = duration;
    }
}
