package fr.dupont.models;

import java.util.List;

public record Compilation(List<Video> videos, Thumbnail thumbnail) {

    public Float getDuration() {
        return videos.stream().map(Video::getDuration).reduce(Float::sum).orElse(0.0f);
    }
}
