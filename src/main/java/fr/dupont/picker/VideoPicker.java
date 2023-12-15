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
import fr.dupont.filter.videofilter.AndVideoFilter;
import fr.dupont.filter.videofilter.DurationFilter;
import fr.dupont.filter.videofilter.QualityFilter;
import fr.dupont.localfiles.FolderUtils;
import fr.dupont.models.Media;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Video;
import fr.dupont.repositories.RedditRepository;
import fr.dupont.videomanipulation.MergeMediaFiles;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class VideoPicker {

    private final RedditRepository redditRepository = new RedditRepository();
    private final ColorLogger logger = new ColorLogger();
    private final int videoDuration;
    private final List<Subreddit> subreddits;

    /**
     * Pick videos from subreddits, download them and merge them.
     * Print a progress bar per subreddit.
     */
    public List<Video> pickVideos() {
        ArrayList<Video> videoCollection = new ArrayList<>();

        subreddits.forEach(subreddit -> {
            final RedditBinder redditBinder = new RedditBinder(subreddit);

            try {
                final ArrayList<Media> medias = (ArrayList<Media>) redditBinder.parse(redditRepository.getTopMediaListOfASub(subreddit));
                final List<Video> videos = filterVideos(medias);
                videoCollection.addAll(mergeAndClean(videos));

            } catch (FailedToGetList | JsonProcessingException | EmptyApiResponse e) {
                e.printStackTrace();
            }

        });

        return videoCollection;
    }

    /**
     * Filter videos to find the best videos with subreddit constraint
     * @param medias List of medias
     * @return a list of Video filtered
     */
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

    /**
     * Merge audio and video and clean the local file
     * to only keep the merged file.
     * Print a progress bar.
     * @param videos List of medias
     */
    public List<Video> mergeAndClean(final List<Video> videos) {
        final MergeMediaFiles merger = new MergeMediaFiles();
        final int subPosition = subreddits.indexOf(videos.get(0).getSubreddit());
        final float maxDuration = videos.get(0).getSubreddit().percentage() * videoDuration;
        float duration = 0F;

        ArrayList<Video> downloaded = new ArrayList<>();

        for (Video video : videos) {
            if (duration > maxDuration)
                return downloaded;

            duration += video.getDuration();

            try {
                redditRepository.downloadMedia(video);
                downloaded.add(video);
            } catch (FailedToRetrievedMedia e) {
                FolderUtils.deleteFile(video.getLocalUrl());
            }

            merger.mergeAudioAndVideo(video);

            FolderUtils.deleteFile(video.getLocalAudioUrl());
            FolderUtils.deleteFile(video.getLocalUrl());
            FolderUtils.moveFile(video.getLocalUrl().replace(".mp4", "-merged.mp4"), video.getLocalUrl());

            logger.print(ColorLogger.Level.INFO, "Subreddit: " + (subPosition+1) + "/" + subreddits.size() + " | " + Math.min((duration / maxDuration) * 100, 100F) + "%");

        }

        return downloaded;
    }


}
