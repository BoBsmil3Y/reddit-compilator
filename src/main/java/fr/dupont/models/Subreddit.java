package fr.dupont.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record Subreddit(String name, Float percentage, int minDurationSeconds, int maxDurationSeconds, int minUps) { }