package fr.dupont.filter;

import fr.dupont.models.Video;

import java.util.List;

public class AndVideoFilter implements VideoFilter {

    private final List<VideoFilter> videoFilters;

    public AndVideoFilter(VideoFilter... videoFilters) {
        this.videoFilters = List.of(videoFilters);
    }

    @Override
    public List<Video> apply(List<Video> videos) {
        return videoFilters.stream()
                .reduce(videos, (mediaList, filter) -> filter.apply(mediaList), (mediaList1, mediaList2) -> mediaList1);
    }

}
