package fr.dupont.filter;

import fr.dupont.models.Video;

import java.util.List;

public interface VideoFilter {

    public List<Video> apply(List<Video> videos);

}
