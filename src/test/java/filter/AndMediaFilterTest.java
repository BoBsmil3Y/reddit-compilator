package filter;

import fr.dupont.filter.mediafilter.AndMediaFilter;
import fr.dupont.filter.MediaFilter;
import fr.dupont.filter.mediafilter.GradeMediaFilter;
import fr.dupont.filter.mediafilter.NsfwMediaFilter;
import fr.dupont.models.Grade;
import fr.dupont.models.Media;
import fr.dupont.models.Thumbnail;
import fr.dupont.models.Video;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class AndMediaFilterTest {

    List<Media> medias;
    Thumbnail thumbnail;
    Video video, videoNsfw, badVideo;
    MediaFilter mediaFilter = new AndMediaFilter(new GradeMediaFilter(), new NsfwMediaFilter());

    @Before
    public void setUp() {
        thumbnail = new Thumbnail("", "", "", "", LocalDateTime.MAX,  false,
                new Grade(500, 600, 500f/600f), null);
        video = new Video("", "", "", "", "", LocalDateTime.MAX, false,
                null, new Grade(500, 10000, 500f/10000f), 10f, null);
        videoNsfw = new Video("", "", "", "", "", LocalDateTime.MAX, true,
                null, new Grade(200, 200, 1.0f), 10f, null);
        badVideo = new Video("", "", "", "", "", LocalDateTime.MAX, true,
                null, new Grade(200, 200, 500f/10000f), 10f, null);

        medias = List.of(video, videoNsfw, thumbnail, badVideo);
    }

    @Test
    public void testApplyAddFilter() {

        List<Media> filteredMedias = mediaFilter.apply(medias);

        assert filteredMedias.size() == 1;
        assert filteredMedias.get(0).equals(thumbnail);

    }

}
