package fr.dupont.binder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.Main;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.models.*;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is used to parse the response of the Reddit API.
 * Return a list of Media, can be a Video or a Thumbnail.
 */
@AllArgsConstructor
public class RedditBinder {

    private static String REGEX_ALPHANUM = "[^a-zA-Z0-9]";
    private Subreddit subreddit;

    /**
     * Parse the response of the Reddit API to get the media list.
     * @param apiResponse The response of the Reddit API.
     * @return The media list.
     */
    public List<Media> parse(final String apiResponse) throws JsonProcessingException, EmptyApiResponse {

        final ArrayList<Media> medias = new ArrayList<>();
        final JsonNode rootNode = new ObjectMapper().readTree(apiResponse).path("data").path("children");

        if (rootNode.isEmpty())
            throw new EmptyApiResponse();

        Iterator<JsonNode> children = rootNode.elements();
        while(children.hasNext()){
            final JsonNode child = children.next().path("data");

            JsonNode mediaType = child.get("media").get("type");
            if (mediaType != null && mediaType.asText().contains("youtube"))
                continue;

            if (child.get("url_overridden_by_dest").asText().contains("v.redd.it")) {
                medias.add(parseVideo(child));
            } else {
                medias.add(parseImage(child));
            }
        }

        return medias;
    }

    /**
     * Parse a JsonNode to get the video information.
     * @param child The JsonNode to parse.
     * @return The video information.
     */
    private Video parseVideo(final JsonNode child) {

        final JsonNode videoInfo = child.path("secure_media").path("reddit_video");
        final String title = child.path("title").asText().replaceAll(REGEX_ALPHANUM, "_");

        final MediaFormat mediaFormat = new MediaFormat(
                videoInfo.path("height").asInt(),
                videoInfo.path("width").asInt()
        );

        final Grade grade = new Grade(
                child.path("score").asInt(),
                child.path("ups").asInt(),
                child.path("upvote_ratio").floatValue()
        );

        return new Video(
                title,
                child.path("author").asText(),
                videoInfo.path("fallback_url").asText(),
                String.format("%s%s.mp4", Main.OUTPUT_FOLDER, title),
                String.format("%s%s-audio.mp4", Main.OUTPUT_FOLDER, title),
                parseDate(child.path("created").asInt()),
                child.path("over_18").asBoolean(),
                mediaFormat,
                grade,
                (float) videoInfo.path("duration").asInt(),
                this.subreddit
        );
    }

    /**
     * Parse a JsonNode to get the image information.
     * @param child The JsonNode to parse.
     * @return The image information.
     */
    private Thumbnail parseImage(final JsonNode child) {

        final Grade grade = new Grade(
                child.path("score").asInt(),
                child.path("ups").asInt(),
                child.path("upvote_ratio").floatValue()
        );

        final String url = child.path("url").asText();
        char[] extension = new char[4];
        url.getChars(url.lastIndexOf('.')+1, url.length(), extension, 0);

        return new Thumbnail(
                child.path("title").asText(),
                child.path("author").asText(),
                url,
                String.format("%s%s.%s", Main.OUTPUT_FOLDER, child.path("title").asText().replaceAll(REGEX_ALPHANUM, "_"), String.valueOf(extension)),
                parseDate(child.path("created").asInt()),
                child.path("over_18").asBoolean(),
                grade,
                this.subreddit
        );
    }

    /**
     * Parse the epoch time to get the date.
     * @param epochTime The epoch time to parse.
     * @return The date.
     */
    private LocalDateTime parseDate(final int epochTime) {
        return LocalDateTime.ofEpochSecond(epochTime, 0, ZoneId.of("Europe/Berlin").getRules().getOffset(Instant.now()));
    }

}
