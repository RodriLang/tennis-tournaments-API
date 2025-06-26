package dev.rodrilang.tennis_tournaments.exceptions;

public class InvalidResultException extends RuntimeException {

    public InvalidResultException(String message) {
        super(message);
    }
}