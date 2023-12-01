package fr.dupont.filter.mediafilter;

import fr.dupont.filter.MediaFilter;
import fr.dupont.models.Media;

import java.util.List;

public class AndMediaFilter implements MediaFilter {

    private final List<MediaFilter> mediaFilters;

    public AndMediaFilter(MediaFilter... mediaFilters) {
        this.mediaFilters = List.of(mediaFilters);
    }

    @Override
    public List<Media> apply(List<Media> medias) {
        return mediaFilters.stream()
                .reduce(medias, (mediaList, filter) -> filter.apply(mediaList), (mediaList1, mediaList2) -> mediaList1);
    }

}
