package fr.dupont.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Media {

    private final String title;
    private final String author;
    private final String url;
    private final String localUrl;
    private final LocalDateTime createdEpochTime;
    private final boolean over18;

    private final Grade grade;
    private final Subreddit subreddit;

}
