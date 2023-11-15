package fr.dupont.binder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.Main;
import fr.dupont.exceptions.EmptyApiResponse;
import fr.dupont.models.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedditBinder {

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
    public Video parseVideo(final JsonNode child) {

        final JsonNode videoInfo = child.path("secure_media").path("reddit_video");
        final String title = child.path("title").asText();

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
                String.format("%s%s.mp4", Main.FULL_OUTPUT_FOLDER, title.replaceAll("[^a-zA-Z0-9]", "_")),
                String.format("%s%s-audio.mp4", Main.FULL_OUTPUT_FOLDER, title.replaceAll("[^a-zA-Z0-9]", "_")),
                child.path("created").asInt(),
                child.path("over_18").asBoolean(),
                mediaFormat,
                grade,
                (float) videoInfo.path("duration").asInt()
        );
    }

    /**
     * Parse a JsonNode to get the image information.
     * @param child The JsonNode to parse.
     * @return The image information.
     */
    public Thumbnail parseImage(final JsonNode child) {

        final Grade grade = new Grade(
                child.path("score").asInt(),
                child.path("ups").asInt(),
                child.path("upvote_ratio").floatValue()
        );

        return new Thumbnail(
                child.path("title").asText(),
                child.path("author").asText(),
                child.path("url").asText(),
                String.format("%s%s.jpg", Main.FULL_OUTPUT_FOLDER, child.path("url").asText().replaceAll("[^a-zA-Z0-9]", "_")),
                child.path("created").asInt(),
                child.path("over_18").asBoolean(),
                grade
        );
    }

}
