package fr.dupont.filter;

import fr.dupont.models.Media;

import java.util.List;

public interface MediaFilter  {

    List<Media> apply(List<Media> medias);

}
