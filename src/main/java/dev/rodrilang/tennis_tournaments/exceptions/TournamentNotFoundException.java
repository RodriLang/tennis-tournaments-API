package dev.rodrilang.tennis_tournaments.exceptions;

public class TournamentNotFoundException extends EntityNotFoundException {

    public TournamentNotFoundException(Long id) {
        super("No tournament was found with the given ID: " + id);
    }

    public TournamentNotFoundException(String message) {
        super(message);
    }
}