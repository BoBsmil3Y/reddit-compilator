package fr.dupont.filter.mediafilter;

import fr.dupont.filter.MediaFilter;
import fr.dupont.models.Media;

import java.util.List;

/**
 * Filter medias to exclude NSFW posts.
 */
public class NsfwMediaFilter implements MediaFilter {

    @Override
    public List<Media> apply(List<Media> medias) {
        return medias.stream()
                .filter(media -> !media.isOver18())
                .toList();
    }

}
