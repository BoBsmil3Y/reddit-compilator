package fr.dupont.binder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.models.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedditBinder {

    public ArrayList<Media> parse(String apiResponse) throws JsonProcessingException {

        ArrayList<Media> medias = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(apiResponse).path("data").path("children");

        if (rootNode.size() == 0)
            throw new RuntimeException("No media found in the response.");

        Iterator<JsonNode> children = rootNode.elements();
        while(children.hasNext()){
            JsonNode child = children.next().path("data");

            if (child.get("url_overridden_by_dest").asText().contains("v.redd.it")) {
                medias.add(parseVideo(child));
            } else {
                medias.add(parseImage(child));
            }
        }

        return medias;
    }

    public Video parseVideo(JsonNode child) {

        JsonNode videoInfo = child.path("secure_media").path("reddit_video");

        MediaFormat mediaFormat = new MediaFormat(
                videoInfo.path("height").asInt(),
                videoInfo.path("width").asInt()
        );

        Grade grade = new Grade(
                child.path("score").asInt(),
                child.path("ups").asInt(),
                child.path("upvote_ratio").floatValue()
        );

        return new Video(
                child.path("title").asText(),
                child.path("author").asText(),
                videoInfo.path("fallback_url").asText(),
                child.path("created").asInt(),
                child.path("over_18").asBoolean(),
                mediaFormat,
                grade,
                (float) videoInfo.path("duration").asInt()
        );
    }

    public Thumbnail parseImage(JsonNode child) {

        JsonNode imageInfo = child.path("preview").path("images").path("variants").path("gif");

        Grade grade = new Grade(
                child.path("score").asInt(),
                child.path("ups").asInt(),
                child.path("upvote_ratio").floatValue()
        );

        return new Thumbnail(
                child.path("title").asText(),
                child.path("author").asText(),
                child.path("url").asText(),
                child.path("created").asInt(),
                child.path("over_18").asBoolean(),
                grade
        );
    }

}
