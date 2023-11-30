package fr.dupont.filter;

import fr.dupont.models.Media;

import java.util.List;

public class AndFilter implements MediaFilter {

    private final List<MediaFilter> mediaFilters;

    public AndFilter(MediaFilter... mediaFilters) {
        this.mediaFilters = List.of(mediaFilters);
    }

    @Override
    public List<Media> apply(List<Media> medias) {
        return mediaFilters.stream()
                .reduce(medias, (mediaList, filter) -> filter.apply(mediaList), (mediaList1, mediaList2) -> mediaList1);
    }

}
