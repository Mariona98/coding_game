package com.example;

/**
 * Immutable configuration for a level: map dimensions, object locations,
 * dialogue, background image, and per-tile movement messages.
 * Create one configuration per level and pass it to {@link Level_1_Mechanics}.
 */
public final class GameLevelData {

    /** Describes whether a map tile can be entered and any message shown there. */
    public static final class Tile {
        private final boolean walkable;
        private final String message;

        public Tile(boolean walkable, String message) {
            this.walkable = walkable;
            this.message = message;
        }

        public boolean isWalkable() {
            return walkable;
        }

        public String getMessage() {
            return message;
        }
    }

    /** One level-specific entry shown in the in-game command reference. */
    public static final class HelpSection {
        private final String heading;
        private final String explanation;
        private final String example;

        public HelpSection(String heading, String explanation, String example) {
            this.heading = heading;
            this.explanation = explanation;
            this.example = example;
        }

        public String getHeading() {
            return heading;
        }

        public String getExplanation() {
            return explanation;
        }

        public String getExample() {
            return example;
        }
    }

    private final int width;
    private final int height;
    private final int heroStartX;
    private final int heroStartY;
    private final int buttonX;
    private final int buttonY;
    private final int doorX;
    private final int doorY;
    private final String backgroundPath;
    private final String boundaryMessage;
    private final String[] dialogues;
    private final String commandHelpIntroduction;
    private final HelpSection[] commandHelpSections;
    private final Tile[][] tiles;

    public GameLevelData(
        int width,
        int height,
        int heroStartX,
        int heroStartY,
        int buttonX,
        int buttonY,
        int doorX,
        int doorY,
        String backgroundPath,
        String boundaryMessage,
        String[] dialogues,
        String commandHelpIntroduction,
        HelpSection[] commandHelpSections,
        Tile[][] tiles
    ) {
        // Validate the map before copying it so later gameplay can trust its dimensions.
        if (width <= 0 || height <= 0 || tiles == null || tiles.length != height) {
            throw new IllegalArgumentException("Tile table dimensions do not match the level size.");
        }
        if (dialogues == null
            || commandHelpIntroduction == null
            || commandHelpSections == null) {
            throw new IllegalArgumentException("Level dialogue and command help must be configured.");
        }
        for (HelpSection section : commandHelpSections) {
            if (section == null) {
                throw new IllegalArgumentException("Every command-help section must be configured.");
            }
        }
        for (Tile[] row : tiles) {
            if (row == null || row.length != width) {
                throw new IllegalArgumentException("Every tile-table row must match the level width.");
            }
            for (Tile tile : row) {
                if (tile == null) {
                    throw new IllegalArgumentException("Every tile-table cell must be configured.");
                }
            }
        }
        validatePosition(heroStartX, heroStartY, width, height, "hero start");
        validatePosition(buttonX, buttonY, width, height, "button");
        validatePosition(doorX, doorY, width, height, "door");

        this.width = width;
        this.height = height;
        this.heroStartX = heroStartX;
        this.heroStartY = heroStartY;
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.doorX = doorX;
        this.doorY = doorY;
        this.backgroundPath = backgroundPath;
        this.boundaryMessage = boundaryMessage;
        this.dialogues = dialogues.clone();
        this.commandHelpIntroduction = commandHelpIntroduction;
        this.commandHelpSections = commandHelpSections.clone();
        this.tiles = new Tile[height][width];
        for (int row = 0; row < height; row++) {
            this.tiles[row] = tiles[row].clone();
        }
    }

    public static Tile[][] createOpenTileTable(int width, int height) {
        // New levels can start with a fully walkable map, then override special tiles.
        Tile[][] tiles = new Tile[height][width];
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                tiles[row][column] = new Tile(true, null);
            }
        }
        return tiles;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHeroStartX() {
        return heroStartX;
    }

    public int getHeroStartY() {
        return heroStartY;
    }

    public int getButtonX() {
        return buttonX;
    }

    public int getButtonY() {
        return buttonY;
    }

    public int getDoorX() {
        return doorX;
    }

    public int getDoorY() {
        return doorY;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public String getBoundaryMessage() {
        return boundaryMessage;
    }

    public String[] getDialogues() {
        return dialogues.clone();
    }

    public String getCommandHelpIntroduction() {
        return commandHelpIntroduction;
    }

    public HelpSection[] getCommandHelpSections() {
        return commandHelpSections.clone();
    }

    public Tile getTile(int x, int y) {
        // Out-of-map coordinates are represented by null and handled as boundaries.
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[y][x];
    }

    private static void validatePosition(
        int x,
        int y,
        int width,
        int height,
        String name
    ) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(name + " position is outside the tile table.");
        }
    }
}
