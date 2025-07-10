package dev.rodrilang.tennis_tournaments.utils;

import dev.rodrilang.tennis_tournaments.enums.StatusType;
import dev.rodrilang.tennis_tournaments.exceptions.DuplicatePlayerException;
import dev.rodrilang.tennis_tournaments.exceptions.InvalidTournamentStatusException;
import dev.rodrilang.tennis_tournaments.exceptions.TournamentFullException;
import dev.rodrilang.tennis_tournaments.models.Player;
import dev.rodrilang.tennis_tournaments.models.Tournament;

public class TournamentValidator {

    private TournamentValidator(){}

    public static void validateTournamentNotStarted(Tournament tournament) {
        if (!tournament.getStatus().equals(StatusType.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("Tournament already started");
        }
    }

    public static void validateTournamentInProgress(Tournament tournament) {
        if (tournament.getStatus().equals(StatusType.NOT_STARTED)) {
            throw new InvalidTournamentStatusException("Tournament has not yet started");
        }
        validateTournamentNotFinished(tournament);
    }

    public static void validateTournamentNotFinished(Tournament tournament) {
        if (tournament.getStatus().equals(StatusType.FINISHED)) {
            throw new InvalidTournamentStatusException("Tournament has already ended");
        }
    }

    public static void validateModifiableStatus(Tournament tournament) {

        validateTournamentNotFinished(tournament);
        validateTournamentNotStarted(tournament);
    }

    public static void validateTournamentIsReadyToStart(Tournament tournament) {
        if (tournament.getPlayers().size() != 16) {
            throw new InvalidTournamentStatusException("Tournament needs exactly 16 players to start.");
        }
    }

    public static void validateTournamentHasRoom(Tournament tournament) {
        if (tournament.getPlayers().size() >= 16) {
            throw new TournamentFullException("Tournament is full.");
        }
    }

    public static void validateNonDuplicatedPlayer(Tournament tournament, Player player) {
        if (tournament.getPlayers().contains(player)) {
            throw new DuplicatePlayerException(player.getDni());
        }
    }

}
