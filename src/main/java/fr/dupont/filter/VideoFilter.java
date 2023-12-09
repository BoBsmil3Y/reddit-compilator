package fr.dupont.filter;

import fr.dupont.models.Media;
import fr.dupont.models.Video;

import java.util.List;
import java.util.logging.Filter;

public interface VideoFilter {

    List<Video> apply(List<Video> videos);

}
