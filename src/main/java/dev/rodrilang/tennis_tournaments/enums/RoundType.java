package dev.rodrilang.tennis_tournaments.enums;

public enum RoundType {
    FIRST, QUARTER_FINAL, SEMI_FINAL, FINAL;

    public RoundType next() {
        return switch (this) {
            case FIRST -> QUARTER_FINAL;
            case QUARTER_FINAL -> SEMI_FINAL;
            case SEMI_FINAL -> FINAL;
            case FINAL -> null; // No hay más rondas
        };
    }


}
