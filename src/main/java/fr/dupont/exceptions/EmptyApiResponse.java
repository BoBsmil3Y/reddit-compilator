package fr.dupont.exceptions;

public class EmptyApiResponse extends Exception {
    public EmptyApiResponse() {
        super("No media found in the response.");
    }

}
