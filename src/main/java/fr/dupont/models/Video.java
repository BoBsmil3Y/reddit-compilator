package fr.dupont.models;

import lombok.Getter;

@Getter
public class Video extends Media {

    private final Float duration;

    public Video(String title, String author, String url, String permaLink, Float duration) {
        super(title, author, url, permaLink);
        this.duration = duration;
    }

}
