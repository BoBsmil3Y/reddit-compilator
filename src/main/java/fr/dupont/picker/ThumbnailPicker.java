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
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Pick the best thumbnail from a list of subreddits.
 */
@AllArgsConstructor
public class ThumbnailPicker {

    private final RedditRepository redditRepository = new RedditRepository();
    private final ColorLogger logger = new ColorLogger();
    private final List<Subreddit> subreddits;

    /**
     * Pick the best thumbnail from a list of subreddits.
     * @return the downloaded thumbnail
     */
    public Thumbnail pickThumbnails() {
        final ArrayList<Thumbnail> allThumbnails = new ArrayList<>();

        subreddits.forEach(subreddit -> {
            final RedditBinder redditBinder = new RedditBinder(subreddit);

            try {
                final List<Media> medias = redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
                final ArrayList<Thumbnail> thumbnailsToChoose = sortAndPickThumbnail(filterThumbnails(medias));
                allThumbnails.add(sortAndPickThumbnail(thumbnailsToChoose).getFirst());

            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse | NoSuchElementException e) {
                logger.print(ColorLogger.Level.ERROR, "Failed to get list of media from " + subreddit.name() + " subreddit.");
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
        final AndMediaFilter andMediaFilter = new AndMediaFilter(new GradeMediaFilter( 0.7F ), new NsfwMediaFilter(), new TodayFilter());

        medias = andMediaFilter.apply(medias);

        return medias.stream()
                .filter(Thumbnail.class::isInstance)
                .map(Thumbnail.class::cast)
                .toList();
    }

    /**
     * Sort thumbnails by ups.
     * @param thumbnails List of thumbnails
     * @return the best thumbnail
     */
    private ArrayList<Thumbnail> sortAndPickThumbnail(List<Thumbnail> thumbnails) {
        ArrayList<Thumbnail> sortedThumbnails = new ArrayList<>(thumbnails);
        sortedThumbnails.sort(
                (t0, t1) ->
                     Integer.compare(t1.getGrade().ups(), t0.getGrade().ups())
                );

        return sortedThumbnails;
    }

    /**
     * Download the best thumbnail.
     * @param sortedThumbnails List of sorted thumbnails
     * @return the downloaded thumbnail. Null if all thumbnails failed to download.
     */
    private Thumbnail downloadThumbnail(final List<Thumbnail> sortedThumbnails) {
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
