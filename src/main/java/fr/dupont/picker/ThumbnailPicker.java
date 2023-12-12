package fr.dupont.picker;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.binder.RedditBinder;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.filter.mediafilter.AndMediaFilter;
import fr.dupont.filter.mediafilter.GradeMediaFilter;
import fr.dupont.filter.mediafilter.NsfwMediaFilter;
import fr.dupont.filter.mediafilter.TodayFilter;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.Media;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Thumbnail;
import fr.dupont.models.Video;
import fr.dupont.repositories.RedditRepository;
import fr.dupont.videomanipulation.MergeMediaFiles;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailPicker {

    private final RedditRepository redditRepository = new RedditRepository();
    private final List<Subreddit> subreddits;

    public ThumbnailPicker(List<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    public void pickThumbnails() {
        subreddits.forEach(subreddit -> {
            final RedditBinder redditBinder = new RedditBinder(subreddit);

            try {
                final ArrayList<Media> medias = (ArrayList<Media>) redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
                ArrayList<Thumbnail> thumbnails = new ArrayList<>(filterThumbnails(medias));

                thumbnails.sort(
                    (t0, t1) -> {
                        if (t0.getGrade().ups() >= t1.getGrade().ups())
                            return -1;
                        else
                            return 1;
                    }
                );

                redditRepository.downloadMedia(thumbnails.get(0));
            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
                throw new RuntimeException(e);
            } catch (FailedToRetrievedMedia e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Filter medias to find the best thumbnails.
     * @param medias List of medias
     * @return a list of Media filtered
     */
    public List<Thumbnail> filterThumbnails(List<Media> medias) {
        final AndMediaFilter andMediaFilter = new AndMediaFilter(new GradeMediaFilter(), new NsfwMediaFilter(), new TodayFilter());

        medias = andMediaFilter.apply(medias);

        return medias.stream()
                .filter(Thumbnail.class::isInstance)
                .map(Thumbnail.class::cast)
                .toList();
    }

}
