package fr.dupont.filter.videofilter;

import fr.dupont.filter.VideoFilter;
import fr.dupont.models.Video;

import java.util.List;

/**
 * Filter to only take videos that are greater than
 * 720 pixels wide.
 */
public class QualityFilter implements VideoFilter {

    @Override
    public List<Video> apply(List<Video> videos) {
        return videos.stream()
                .filter(video -> video.getFormat().width() >= 720 )
                .toList();
    }

}
