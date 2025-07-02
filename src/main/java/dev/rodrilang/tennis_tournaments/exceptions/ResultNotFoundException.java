package dev.rodrilang.tennis_tournaments.exceptions;

public class ResultNotFoundException extends EntityNotFoundException {

    public ResultNotFoundException(Long id) {
        super("No result was found with the given ID: " + id);
    }

    public ResultNotFoundException(String message) {
        super(message);
    }
}