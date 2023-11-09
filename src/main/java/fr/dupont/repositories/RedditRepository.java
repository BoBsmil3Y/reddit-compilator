package fr.dupont.repositories;

import fr.dupont.ColorLogger;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.models.Subreddit;
import fr.dupont.models.Video;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Repository to get data from Reddit.
 */
public class RedditRepository {

    private final int MAX_VIDEOS_PER_SUBREDDIT = 50;
    private final int REQUEST_ATTEMPT = 3;
    private final HttpClient client  = HttpClient.newHttpClient();
    private final ColorLogger logger = new ColorLogger();

    /**
     * Get the top videos of a subreddit.
     * @param subreddit The subreddit to get the top videos from.
     * @return The response body of the request.
     */
    public String getTopVideoListOfASub(Subreddit subreddit) {
        int count = 0;

        while (count < REQUEST_ATTEMPT) {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://www.reddit.com/r/%s/top.json?sort=top&t=day&limit=%s", subreddit.name(), MAX_VIDEOS_PER_SUBREDDIT)))
                        .header("accept", "application/json")
                        .timeout(Duration.of(5, SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                logger.print(ColorLogger.Level.INFO, String.format("Response code: %s", response.statusCode()));
                return response.body();

            } catch (IOException | InterruptedException e) {
                logger.print(ColorLogger.Level.ERROR, String.format("Failed to get top videos of %s ! Retrying ... %n%s", subreddit.name(), e.getMessage()));
                count++;
            }
        }

        throw new RuntimeException("Failed to get top videos of " + subreddit.name());
    }

    /**
     * Download a video from the url of the video object.
     * @param video The video to download.
     * @throws FailedToRetrievedMedia If the video couldn't be retrieved.
     */
    public void getVideo(Video video) throws FailedToRetrievedMedia {
        int responseCode = 0;
        final String path = String.format("./output/downloaded/%s.mp4", video.getTitle().replaceAll("[^a-zA-Z0-9]", "_" ));

        responseCode = requestMedia(video.getUrl(), path);

        if (responseCode != 200)
            throw new FailedToRetrievedMedia(video.getTitle(), video.getUrl(), responseCode);
    }

    /**
     * Download the audio from the url of the video object.
     * @param video The video to get the audio from.
     * @throws FailedToRetrievedMedia If the audio couldn't be retrieved.
     */
    public void getAudio(Video video) throws FailedToRetrievedMedia {
        final String path = String.format("./output/downloaded/%s-audio.mp4", video.getTitle().replaceAll("[^a-zA-Z0-9]", "_" ));
        final String[] suffixes = { "DASH_AUDIO_128","DASH_AUDIO_64" };
        int responseCode = 0;

        for(int i = 0; i < suffixes.length; i++){
            String url = video.getUrl()
                            .replaceAll("DASH_[0-9]+", suffixes[i])
                            .replace("?source=fallback", "");
            responseCode = requestMedia(url, path);
            if (responseCode == 200)
                return;
        }

        throw new FailedToRetrievedMedia(video.getTitle(), video.getUrl(), responseCode);
    }

    /**
     * Request a media from an url and save it to a path.
     * @param url the url of the media.
     * @param path the local path to save the media.
     * @return the response code of the request.
     */
    private int requestMedia(String url, String path) {
        int requestCount = 0;
        OutputStream output = null;
        InputStream input = null;
        HttpResponse<InputStream> response;

        while (requestCount < REQUEST_ATTEMPT) {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.of(5, SECONDS))
                        .GET()
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                if (response.statusCode() > 400  && response.statusCode() < 408)
                    return response.statusCode();

                if (response.statusCode() != 200)
                    continue;

                input = response.body();
                output = new FileOutputStream(path);
                byte[] data = new byte[4096];
                int count;

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                logger.print(ColorLogger.Level.SUCCESS, String.format("Media retrieved → %s", url));
                return response.statusCode();

            } catch (IOException | InterruptedException e) {
                logger.print(ColorLogger.Level.ERROR, String.format("Failed to get media → %s! Retrying ... %n%s", url, e.getMessage()));
                requestCount++;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();

                } catch (IOException ignored) { }
            }

        }

        return -1;
    }

}
