package fr.dupont.filter.mediafilter;

import fr.dupont.filter.MediaFilter;
import fr.dupont.models.Media;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Filter medias to filter on today's date.
 */
public class TodayFilter implements MediaFilter {

    LocalDateTime today = LocalDateTime.now();

    @Override
    public List<Media> apply(List<Media> medias) {
        return medias.stream()
                .filter(media -> media.getCreatedEpochTime().isAfter(today.minusDays(1)))
                .toList();
    }

}
