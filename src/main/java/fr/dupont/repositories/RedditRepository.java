package fr.dupont.repositories;

import fr.dupont.ColorLogger;
import fr.dupont.models.Subreddit;

import java.io.IOException;
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

    private final int MAX_VIDEOS_PER_SUBREDDIT = 1;
    private final int REQUEST_ATTEMPT = 3;
    private final HttpClient client  = HttpClient.newHttpClient();
    private final ColorLogger logger = new ColorLogger();

    /**
     * Get the top videos of a subreddit.
     * @param subreddit The subreddit to get the top videos from.
     * @return The response body of the request.
     */
    public String getTopVideosOfASub(Subreddit subreddit) {
        int count = 0;

        while (count < REQUEST_ATTEMPT) {

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(String.format("https://www.reddit.com/r/%s/top.json?sort=top&t=day&limit=100&limit=%s", subreddit.name(), MAX_VIDEOS_PER_SUBREDDIT)))
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

}
