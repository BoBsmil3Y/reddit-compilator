package fr.dupont.models;

import java.util.List;

public record Config (int video_number, int video_duration, String video_base_name, List<Subreddit> subreddits) { }