package fr.dupont.filter.videofilter;

import fr.dupont.filter.VideoFilter;
import fr.dupont.models.Video;

import java.util.List;

/**
 * Filter to exclude videos that didn't respect time
 * constraint set by the Subreddit.
 */
public class DurationFilter implements VideoFilter {

    @Override
    public List<Video> apply(List<Video> videos) {
        return videos.stream()
                .filter(video -> video.getDuration() > video.getSubreddit().minDurationSeconds() && video.getDuration() < video.getSubreddit().maxDurationSeconds())
                .toList();
    }

}
