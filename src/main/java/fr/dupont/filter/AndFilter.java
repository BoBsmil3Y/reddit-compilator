package fr.dupont.filter;

import fr.dupont.models.Media;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class AndFilter implements Filter {

    private final List<Filter> filters;

    @Override
    public List<Media> apply(List<Media> medias) {
        return filters.stream()
                .reduce(medias, (mediaList, filter) -> filter.apply(mediaList), (mediaList1, mediaList2) -> mediaList1);
    }

}
