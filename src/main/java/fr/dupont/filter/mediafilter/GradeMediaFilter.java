package fr.dupont.filter.mediafilter;

import fr.dupont.filter.MediaFilter;
import fr.dupont.models.Media;
import lombok.Getter;

import java.util.List;

/**
 * Filter medias to take post with good rating.
 */
public class GradeMediaFilter implements MediaFilter {

    private final float minUpVoteRatio = 0.8F;

    @Override
    public List<Media> apply(List<Media> medias) {
        return medias.stream()
                .filter(media -> media.getGrade().ups() >= media.getSubreddit().minUps()
                                    && media.getGrade().upvoteRatio() >= minUpVoteRatio)
                .toList();
    }

}
