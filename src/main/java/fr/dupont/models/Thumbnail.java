package fr.dupont.models;

public class Thumbnail extends Media {

    public Thumbnail(String title, String author, String url ,String localUrl, int createdEpochTime, boolean over18, Grade grade, Subreddit subreddit){
        super(title, author, url, localUrl, createdEpochTime, over18, grade, subreddit);
    }

}
