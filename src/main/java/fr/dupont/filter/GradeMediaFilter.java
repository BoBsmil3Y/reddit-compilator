package fr.dupont.filter;

import fr.dupont.models.Media;
import lombok.Getter;

import java.util.List;

@Getter
public class GradeMediaFilter implements MediaFilter {

    private final float minUpVoteRatio = 0.8F;
    private final int minUpVote = 250;

    @Override
    public List<Media> apply(List<Media> medias) {
        return medias.stream()
                .filter(media -> media.getGrade().upvoteRatio() >= minUpVoteRatio
                                    && media.getGrade().ups() >= minUpVote)
                .toList();
    }

}
