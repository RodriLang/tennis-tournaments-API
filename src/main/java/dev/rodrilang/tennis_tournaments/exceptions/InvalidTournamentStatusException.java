package dev.rodrilang.tennis_tournaments.exceptions;

public class InvalidTournamentStatusException extends RuntimeException {

    public InvalidTournamentStatusException(String message) {
        super(message);
    }
    public InvalidTournamentStatusException() {
        super("El torneo ha finalizado");
    }

}