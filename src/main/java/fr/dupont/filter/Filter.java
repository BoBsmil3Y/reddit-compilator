package fr.dupont.filter;

import fr.dupont.models.Media;

import java.util.List;

public interface Filter {

    List<Media> apply(List<Media> medias);

}
