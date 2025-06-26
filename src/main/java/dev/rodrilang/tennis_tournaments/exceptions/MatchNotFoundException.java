package dev.rodrilang.tennis_tournaments.exceptions;

public class MatchNotFoundException extends EntityNotFoundException {

    public MatchNotFoundException(Long id) {
        super("No match was found with the given ID: " + id);
    }

    public MatchNotFoundException(String message) {
        super(message);
    }
}