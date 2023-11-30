package filter;

import fr.dupont.filter.MediaFilter;
import fr.dupont.filter.GradeMediaFilter;
import fr.dupont.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GradeMediaFilterTest {

    List<Media> medias;
    Thumbnail thumbnail;
    Video video, videoLowUps;
    MediaFilter mediaFilter = new GradeMediaFilter();

    @Before
    public void setUp() {
        thumbnail = new Thumbnail("", "", "", "", 1,  false,
                    new Grade(500, 600, 500f/600f), null);
        video = new Video("", "", "", "", "", 1, false,
                null, new Grade(500, 10000, 500f/10000f), 10f, null);
        videoLowUps = new Video("", "", "", "", "", 1, false,
                null, new Grade(200, 200, 1.0f), 10f, null);

        medias = List.of(video, videoLowUps, thumbnail);
    }

    @Test
    public void testApplyRemoveTooLowGrade() {

        List<Media> res = mediaFilter.apply(medias);

        assertEquals(1, res.size());
        assertEquals(thumbnail, res.get(0));

    }

}
