package dev.rodrilang.tennis_tournaments.enums;

public enum SurfaceType {
    CARPET, CLAY, GRASS, HARD;

    public String getDisplayName() {
        return switch (this) {
            case CARPET -> "Carpeta";
            case CLAY -> "Arcilla";
            case GRASS -> "Césped";
            case HARD -> "Cemento";
        };
    }
}

