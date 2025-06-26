package dev.rodrilang.tennis_tournaments.exceptions;

public class IncompleteMatchException extends RuntimeException {

    public IncompleteMatchException(String message) {
        super(message);
    }

}