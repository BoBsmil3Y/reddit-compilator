package fr.dupont.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record Grade(int score, int ups, Float upvoteRatio) { }
