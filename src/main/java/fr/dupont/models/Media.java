package fr.dupont.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Media {

    private final String title;
    private final String author;
    private final String url;
    private final String permaLink;

}
