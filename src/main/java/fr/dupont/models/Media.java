package fr.dupont.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Media {

    private final String title;
    private final String author;
    private final String url;
    private final int createdEpochTime;
    private final boolean over18;

    private final MediaFormat format;
    private final Grade grade;

}
