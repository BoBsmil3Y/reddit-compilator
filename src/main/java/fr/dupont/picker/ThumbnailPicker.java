package fr.dupont.picker;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.ColorLogger;
import fr.dupont.binder.RedditBinder;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
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
    private final ColorLogger logger = new ColorLogger();
    private final List<Subreddit> subreddits;

    public ThumbnailPicker(List<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    public Thumbnail pickThumbnails() {
        final ArrayList<Thumbnail> allThumbnails = new ArrayList<>();

        subreddits.forEach(subreddit -> {
            final RedditBinder redditBinder = new RedditBinder(subreddit);

            try {
                final List<Media> medias = redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
                final ArrayList<Thumbnail> thumbnailsToChoose = sortAndPickThumbnail(filterThumbnails(medias));
                allThumbnails.add(sortAndPickThumbnail(thumbnailsToChoose).getFirst());

            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
                throw new RuntimeException(e);
            }
        });

        final List<Thumbnail> sortedThumbnails = sortAndPickThumbnail(allThumbnails);
        return downloadThumbnail(sortedThumbnails);
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

    private ArrayList<Thumbnail> sortAndPickThumbnail(List<Thumbnail> thumbnails) {
        ArrayList<Thumbnail> sortedThumbnails = new ArrayList<>(thumbnails);
        sortedThumbnails.sort(
                (t0, t1) ->
                     Integer.compare(t1.getGrade().ups(), t0.getGrade().ups())
                );

        return sortedThumbnails;
    }

    public Thumbnail downloadThumbnail(final List<Thumbnail> sortedThumbnails) {
        int attempt = 0;

        while (attempt < sortedThumbnails.size()) {
            try {
                redditRepository.downloadMedia(sortedThumbnails.get(attempt));
                logger.print(ColorLogger.Level.SUCCESS, "Best thumbnail downloaded successfully! Coming from " + sortedThumbnails.get(attempt).getSubreddit().name() + " subreddit.");
                return sortedThumbnails.get(attempt);

            } catch (FailedToRetrievedMedia e) {
                logger.print(ColorLogger.Level.ERROR, "Failed to download thumbnail from " + sortedThumbnails.get(attempt).getSubreddit().name() + " subreddit.");
                attempt++;
            }
        }

        return null;
    }

}
