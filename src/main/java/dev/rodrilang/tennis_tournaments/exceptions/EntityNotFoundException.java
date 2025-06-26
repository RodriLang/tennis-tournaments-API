package dev.rodrilang.tennis_tournaments.exceptions;

public abstract class EntityNotFoundException extends RuntimeException {

    protected EntityNotFoundException(String message) {
        super(message);
    }
}
