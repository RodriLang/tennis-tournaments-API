package dev.rodrilang.tennis_tournaments.exceptions;

public class PlayerNotFoundException extends EntityNotFoundException {


    public PlayerNotFoundException(Long id) {
        super("No player was found with the ID: " + id);
    }

    public PlayerNotFoundException(String dni) {
        super("No player was found with the ID: " + dni);
    }
}