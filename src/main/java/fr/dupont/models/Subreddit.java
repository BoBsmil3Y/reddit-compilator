package fr.dupont.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Subreddit {

    private final String name;
    private final Float pourcentage;
    private final int minDurationSeconds;
    private final int maxDurationSeconds;

}
