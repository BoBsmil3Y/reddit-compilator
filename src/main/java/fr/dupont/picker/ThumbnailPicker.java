package fr.dupont.picker;

import fr.dupont.models.Subreddit;
import fr.dupont.repositories.RedditRepository;

import java.util.List;

public class ThumbnailPicker {

    private final RedditRepository redditRepository = new RedditRepository();
    private final List<Subreddit> subreddits;

    public ThumbnailPicker(List<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

}
