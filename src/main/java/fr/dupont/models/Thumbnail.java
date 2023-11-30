package fr.dupont.models;

import java.time.LocalDateTime;

public class Thumbnail extends Media {

    public Thumbnail(String title, String author, String url , String localUrl, LocalDateTime createdEpochTime, boolean over18, Grade grade, Subreddit subreddit){
        super(title, author, url, localUrl, createdEpochTime, over18, grade, subreddit);
    }

}
