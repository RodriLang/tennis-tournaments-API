package dev.rodrilang.tennis_tournaments.exceptions;

public class CredentialNotFoundException extends EntityNotFoundException {


    public CredentialNotFoundException() {
        super("Bad credentials");
    }
}