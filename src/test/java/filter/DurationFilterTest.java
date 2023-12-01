package filter;

import fr.dupont.filter.*;
import fr.dupont.filter.videofilter.DurationFilter;
import fr.dupont.models.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class DurationFilterTest {

    List<Video> videos;
    Video video, videoTooLong, videoTooShort;
    VideoFilter videoFilter = new DurationFilter();

    @Before
    public void setUp() {

        video = new Video("", "", "", "", "", LocalDateTime.MAX, false,
                new MediaFormat(720, 1080), null, 15f, new Subreddit("", 1f, 10, 100));

        videoTooLong = new Video("", "", "", "", "", LocalDateTime.MAX, false,
                new MediaFormat(720, 1080), null, 103f, new Subreddit("", 1f, 10, 100));

        videoTooShort = new Video("", "", "", "", "", LocalDateTime.MAX, false,
                new MediaFormat(720, 1080), null, 5f, new Subreddit("", 1f, 10, 100));

        videos = List.of(videoTooShort, video, videoTooLong);
    }

    @Test
    public void testApplyAddFilter() {

        List<Video> filteredMedias = videoFilter.apply(videos);

        assert filteredMedias.size() == 1;
        assert filteredMedias.get(0).equals(video);

    }

}
