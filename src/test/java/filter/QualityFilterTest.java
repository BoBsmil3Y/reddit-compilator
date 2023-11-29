package filter;

import fr.dupont.filter.Filter;
import fr.dupont.filter.GradeFilter;
import fr.dupont.filter.QualityFilter;
import fr.dupont.filter.VideoFilter;
import fr.dupont.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class QualityFilterTest {

    List<Video> videos;
    Video video, videoLowQuality;
    VideoFilter filter = new QualityFilter();

    @Before
    public void setUp() {
        video = new Video("", "", "", "", "", 1, false,
                new MediaFormat(1000, 1000), null, 10f,null);
        videoLowQuality = new Video("", "", "", "", "", 1, false,
                        new MediaFormat(1, 1), null, 10f, null);

        videos = List.of(videoLowQuality, video);
    }

    @Test
    public void testApplyRemoveTooLowGrade() {

        List<Video> res = filter.apply(videos);

        assertEquals(1, res.size());
        assertEquals(video, res.get(0));

    }

}
