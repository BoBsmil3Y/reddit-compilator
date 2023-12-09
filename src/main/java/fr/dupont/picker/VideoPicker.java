package fr.dupont.picker;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dupont.binder.RedditBinder;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.filter.VideoFilter;
import fr.dupont.filter.mediafilter.AndMediaFilter;
import fr.dupont.filter.mediafilter.GradeMediaFilter;
import fr.dupont.filter.mediafilter.NsfwMediaFilter;
import fr.dupont.filter.mediafilter.TodayFilter;
import fr.dupont.filter.videofilter.AndVideoFilter;
import fr.dupont.filter.videofilter.DurationFilter;
import fr.dupont.filter.videofilter.QualityFilter;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.Media;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Video;
import fr.dupont.repositories.RedditRepository;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoPicker {

    final private RedditRepository redditRepository = new RedditRepository();
    final private List<Subreddit> subreddits;

    public VideoPicker(List<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }

    public void pickVideos() {
        subreddits.forEach(subreddit -> {
            final RedditBinder redditBinder = new RedditBinder(subreddit);
            ArrayList<Media> medias = null;

            try {
                medias = (ArrayList<Media>) redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
                e.printStackTrace();
            }

        });
    }

    public List<Video> filterVideos(List<Media> medias) {
        final AndVideoFilter andVideoFilter = new AndVideoFilter(new DurationFilter(), new QualityFilter());
        final AndMediaFilter andMediaFilter = new AndMediaFilter(new GradeMediaFilter(), new NsfwMediaFilter(), new TodayFilter());

        medias = andMediaFilter.apply(medias);

        List<Video> videos = medias.stream()
                .filter(Video.class::isInstance)
                .map(Video.class::cast)
                .toList();

        return andVideoFilter.apply(videos);
    }

}
