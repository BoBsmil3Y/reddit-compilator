package fr.dupont.models;

import lombok.Getter;

@Getter
public class Video extends Media {

    private final Float duration;
    private final MediaFormat format;

    public Video(String title, String author, String url, int createdEpochTime, boolean over18, MediaFormat format, Grade grade, Float duration) {
        super(title, author, url, createdEpochTime, over18, grade);
        this.duration = duration;
        this.format = format;
    }

}
