package fr.dupont.exceptions;

public class EnvVarNotSetException extends Exception {

    public EnvVarNotSetException(String key) {
        super(key + " environment variable is not set");
    }

}
