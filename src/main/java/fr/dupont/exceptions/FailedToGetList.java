package fr.dupont.exceptions;

public class FailedToGetList extends Exception {

    public FailedToGetList(String subredditName, int responseCode) {
        super(String.format("Failed to retrieve a list of media for subreddit : %s. %nResponse code: %s", subredditName, responseCode));
    }

}
