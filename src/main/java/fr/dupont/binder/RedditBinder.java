package fr.dupont.binder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dupont.models.Grade;
import fr.dupont.models.MediaFormat;
import fr.dupont.models.Video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedditBinder {

    public ArrayList<Video> parseVideos(String apiResponse) throws JsonProcessingException {

        ArrayList<Video> videos = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(apiResponse);
        JsonNode childrenRoot = rootNode.path("data").path("children");

        Iterator<JsonNode> childrens = childrenRoot.elements();
        while(childrens.hasNext()){
            JsonNode children = childrens.next().path("data");
            JsonNode videoInfo = children.path("secure_media").path("reddit_video");

            MediaFormat mediaFormat = new MediaFormat(
                    videoInfo.path("height").asInt(),
                    videoInfo.path("width").asInt()
            );
            Grade grade = new Grade(
                    children.path("score").asInt(),
                    children.path("ups").asInt(),
                    children.path("upvote_ratio").floatValue()
            );

            videos.add(
                    new Video(
                            children.path("title").asText(),
                            children.path("author").asText(),
                            videoInfo.path("fallback_url").asText(),
                            children.path("created").asInt(),
                            children.path("over_18").asBoolean(),
                            mediaFormat,
                            grade,
                            (float) videoInfo.path("duration").asInt()
                    )
            );
        }

        return videos;
    }

}
