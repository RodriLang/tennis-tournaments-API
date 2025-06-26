package dev.rodrilang.tennis_tournaments.exceptions;

import lombok.Getter;

@Getter
public class DeletedPlayerException extends EntityNotFoundException {

    private final String playerDni;

    public DeletedPlayerException(String dni) {

        super("There is a deleted player with that DNI: " + dni);
        this.playerDni = dni;

    }

}