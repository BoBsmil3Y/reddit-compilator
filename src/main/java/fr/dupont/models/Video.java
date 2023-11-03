package fr.dupont.models;

public class Video extends Media {

    private Float duration;

    public Video(String title, String author, String url, String permaLink, Float duration) {
        super(title, author, url, permaLink);
        this.duration = duration;
    }

}
