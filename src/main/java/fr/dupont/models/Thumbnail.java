package fr.dupont.models;

public class Thumbnail extends Media {

    public Thumbnail(String title, String author, String url, String permaLink, int createdEpochTime, boolean over18, MediaFormat format, Grade grade) {
        super(title, author, url, permaLink, createdEpochTime, over18, format, grade);
    }
}