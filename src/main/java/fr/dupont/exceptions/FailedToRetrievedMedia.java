package fr.dupont.exceptions;

public class FailedToRetrievedMedia extends Exception {

    public FailedToRetrievedMedia(String title, String url, int responseCode) {
        super(String.format("Failed to retrieve media %s with URL : %s. %nResponse code: %s", title, url, responseCode));
    }

    public FailedToRetrievedMedia(String url) {
        super(String.format("Failed to retrieve media with URL : %s.", url));
    }

}
