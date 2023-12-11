package fr.dupont.picker;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.binder.RedditBinder;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.filter.mediafilter.AndMediaFilter;
import fr.dupont.filter.mediafilter.GradeMediaFilter;
import fr.dupont.filter.mediafilter.NsfwMediaFilter;
import fr.dupont.filter.mediafilter.TodayFilter;
import fr.dupont.models.Media;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Thumbnail;
import fr.dupont.repositories.RedditRepository;

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
                final List<Thumbnail> thumbnails = filterThumbnails(medias);

            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
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
