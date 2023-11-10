package fr.dupont.repositories;

import fr.dupont.ColorLogger;
import fr.dupont.exceptions.FailedToGetList;
import fr.dupont.exceptions.FailedToRetrievedMedia;
import fr.dupont.models.Media;
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

    private final int maxMediaPerSubreddit = 50;
    private final int requestAttempt = 2;
    private final int timeoutSeconds = 4;
    private final String[] suffixes = { "DASH_AUDIO_128","DASH_AUDIO_64" };
    private final HttpClient client  = HttpClient.newHttpClient();
    private final ColorLogger logger = new ColorLogger();

    /**
     * Get the top media list of a subreddit.
     * @param subreddit The subreddit to get the top media from.
     * @return The response body of the request.
     */
    public String getTopMediaListOfASub(Subreddit subreddit) throws FailedToGetList {
        int count = 0;
        int responseCode = 0;

        while (count < requestAttempt) {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://www.reddit.com/r/%s/top.json?sort=top&t=day&limit=%s", subreddit.name(), maxMediaPerSubreddit)))
                        .header("accept", "application/json")
                        .timeout(Duration.of(timeoutSeconds, SECONDS))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseCode = response.statusCode();

                if (responseCode != 200){
                    logger.print(ColorLogger.Level.ERROR, String.format("Failed to get top media of %s ! Retrying ... %d attempts", subreddit.name(), count));
                    continue;
                }

                logger.print(ColorLogger.Level.INFO, String.format("Response code: %s", responseCode));
                return response.body();

            } catch (IOException | InterruptedException e) {
                logger.print(ColorLogger.Level.ERROR, String.format("Failed to get top media of %s ! Retrying ... %n%s", subreddit.name(), e.getMessage()));
                count++;
            }
        }

        throw new FailedToGetList(subreddit.name(), responseCode);
    }

    /**
     * Download a media or an image from the url of the Media object.
     * @param media The media to download.
     * @throws FailedToRetrievedMedia If the media couldn't be retrieved.
     */
    public void downloadMedia(Media media) throws FailedToRetrievedMedia {
        final String extension = media instanceof Video ? "mp4" : "jpg";
        final String url = media.getUrl();
        final String path = String.format("./output/downloaded/%s.%s", media.getTitle().replaceAll("[^a-zA-Z0-9]", "_"), extension);
        final String audioPath = String.format("./output/downloaded/%s-audio.%s", media.getTitle().replaceAll("[^a-zA-Z0-9]", "_"), extension);

        downloadFromUrl(url, path);

        if (! (media instanceof Video))
            return;

        for (String suffix : suffixes) {
            String audioUrl = media.getUrl()
                    .replaceAll("DASH_[0-9]+", suffix)
                    .replace("?source=fallback", "");

            if (downloadFromUrl(audioUrl, audioPath) == 200)
                return;
        }

        throw new FailedToRetrievedMedia(url);
    }

    /**
     * Download from the url a media from Reddit server.
     * @param url The url of the media to download.
     * @param path The path where to save the media.
     * @return The response code of the request.
     */
    private int downloadFromUrl(final String url, final String path){
        int requestCount = 0;
        OutputStream output = null;
        InputStream input = null;
        HttpResponse<InputStream> response = null;

        while (requestCount < requestAttempt) {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.of(timeoutSeconds, SECONDS))
                        .GET()
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

                if (response.statusCode() != 200)
                    throw new IOException();

                input = response.body();
                output = new FileOutputStream(path);
                byte[] data = new byte[4096];
                int count;

                while ((count = input.read(data)) != -1)
                    output.write(data, 0, count);

                logger.print(ColorLogger.Level.SUCCESS, String.format("Media retrieved → %s", url));

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

        assert response != null;
        return response.statusCode();
    }

}
