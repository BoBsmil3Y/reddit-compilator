package fr.dupont.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public record Compilation(List<Video> videos, Thumbnail thumbnail) {

    public Float getDuration() {
        Float count = 0.0f;

        for (Video video : videos)
            count += video.getDuration();

        return count;
    }
}
