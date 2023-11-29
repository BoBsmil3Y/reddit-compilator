package fr.dupont.filter;

import fr.dupont.models.Video;

import java.util.List;

public class QualityFilter implements VideoFilter {

    @Override
    public List<Video> apply(List<Video> videos) {
        return videos.stream()
                .filter(video -> video.getFormat().width() >= 720 )
                .toList();
    }

}
