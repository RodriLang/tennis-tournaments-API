package dev.rodrilang.tennis_tournaments.exceptions;

public class TournamentFullException extends RuntimeException {

    public TournamentFullException(String message) {
        super(message);
    }
}