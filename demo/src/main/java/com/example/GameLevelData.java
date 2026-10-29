package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared immutable structure for describing a level. Each level supplies its
 * own values for the map (indexed as {@code tiles[x][y]}), level objects,
 * dialogue, background, tile messages, and command help; the game screen and
 * mechanics consume the same structure.
 */
public final class GameLevelData {

    /** A level object rendered by the game controller rather than declared in FXML. */
    public static final class LevelObject {
        public enum Type {
            HERO,
            BUTTON,
            DOOR,
            BLOCKER,
            DECORATION
        }

        private final Type type;
        private final String imagePath;
        private final String activatedImagePath;
        private final int x;
        private final int y;
        private final double fitWidth;
        private final double fitHeight;
        private final boolean blocksMovement;
        private final String blockedMessage;

        public LevelObject(
            String imagePath,
            int x,
            int y,
            double fitWidth,
            double fitHeight
        ) {
            this(
                Type.DECORATION,
                imagePath,
                null,
                x,
                y,
                fitWidth,
                fitHeight,
                false,
                null
            );
        }

        public LevelObject(
            String imagePath,
            int x,
            int y,
            double fitWidth,
            double fitHeight,
            String blockedMessage
        ) {
            this(
                Type.BLOCKER,
                imagePath,
                null,
                x,
                y,
                fitWidth,
                fitHeight,
                true,
                blockedMessage
            );
        }

        public LevelObject(
            Type type,
            String imagePath,
            String activatedImagePath,
            int x,
            int y,
            double fitWidth,
            double fitHeight,
            boolean blocksMovement,
            String blockedMessage
        ) {
            if (type == null || imagePath == null || imagePath.isEmpty()) {
                throw new IllegalArgumentException("Level objects need a type and image path.");
            }
            if (x < 0 || y < 0 || fitWidth <= 0 || fitHeight <= 0) {
                throw new IllegalArgumentException("Level object position and size must be positive.");
            }
            this.type = type;
            this.imagePath = imagePath;
            this.activatedImagePath = activatedImagePath;
            this.x = x;
            this.y = y;
            this.fitWidth = fitWidth;
            this.fitHeight = fitHeight;
            this.blocksMovement = blocksMovement;
            this.blockedMessage = blockedMessage;
        }

        public Type getType() {
            return type;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getActivatedImagePath() {
            return activatedImagePath;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getFitWidth() {
            return fitWidth;
        }

        public double getFitHeight() {
            return fitHeight;
        }

        public boolean blocksMovement() {
            return blocksMovement;
        }

        public String getBlockedMessage() {
            return blockedMessage;
        }
    }

    /** Describes whether a map tile can be entered and any message shown there. */
    public static final class Tile {
        private final boolean walkable;
   

        /** Creates a tile with its movement rule and optional arrival message. */
        public Tile(boolean walkable, String message) {
            this.walkable = walkable;
            
        }

        /** Returns whether the hero can enter this tile. */
        public boolean isWalkable() {
            return walkable;
        }


    }

    //Maybe will be used
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
    private final int totemY;
    private final int totemX;
    private final int enemyX;
    private final int enemyY;
    private final int goldMaxSteps;
    private final int goldMaxLines;
    private final String backgroundPath;
    private final String boundaryMessage;
    private final String[] dialogues;
    private final String commandHelpIntroduction;
    private final HelpSection[] commandHelpSections;
    private final Tile[][] tiles;
    private final List<LevelObject> levelObjects;


    public GameLevelData(
        int width,
        int height,
        int heroStartX,
        int heroStartY,
        int buttonX,
        int buttonY,
        int doorX,
        int doorY,
        int totemX,
        int totemY,
        int enemyX,
        int enemyY,
        int goldMaxSteps,
        int goldMaxLines,
        String backgroundPath,
        String boundaryMessage,
        String[] dialogues,
        String commandHelpIntroduction,
        HelpSection[] commandHelpSections,
        Tile[][] tiles,
        LevelObject[] levelObjects
    ) {
        
        if (width <= 0 || height <= 0 || tiles == null || tiles.length != width) {
            throw new IllegalArgumentException("Tile table dimensions do not match the level size.");
        }
        if (goldMaxSteps < 0 || goldMaxLines < 0) {
            throw new IllegalArgumentException("Gold score targets cannot be negative.");
        }
        if (dialogues == null
            || commandHelpIntroduction == null
            || commandHelpSections == null
            || levelObjects == null) {
            throw new IllegalArgumentException("Level dialogue and command help must be configured.");
        }
        for (HelpSection section : commandHelpSections) {
            if (section == null) {
                throw new IllegalArgumentException("Every command-help section must be configured.");
            }
        }
        for (Tile[] column : tiles) {
            if (column == null || column.length != height) {
                throw new IllegalArgumentException("Every tile-table column must match the level height.");
            }
            for (Tile tile : column) {
                if (tile == null) {
                    throw new IllegalArgumentException("Every tile-table cell must be configured.");
                }
            }
        }
        for (LevelObject object : levelObjects) {
            if (object == null
                || object.getX() >= width
                || object.getY() >= height) {
                throw new IllegalArgumentException(
                    "Every level object must be configured inside the level bounds."
                );
            }
        }
        requireObject(levelObjects, LevelObject.Type.HERO, heroStartX, heroStartY);
        requireObject(levelObjects, LevelObject.Type.BUTTON, buttonX, buttonY);
        requireObject(levelObjects, LevelObject.Type.DOOR, doorX, doorY);

        this.width = width;
        this.height = height;
        this.heroStartX = heroStartX;
        this.heroStartY = heroStartY;
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.doorX = doorX;
        this.doorY = doorY;
        this.totemX = totemX;
        this.totemY = totemY;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.goldMaxSteps = goldMaxSteps;
        this.goldMaxLines = goldMaxLines;
        this.backgroundPath = backgroundPath;
        this.boundaryMessage = boundaryMessage;
        this.dialogues = dialogues.clone();
        this.commandHelpIntroduction = commandHelpIntroduction;
        this.commandHelpSections = commandHelpSections.clone();
        this.tiles = new Tile[width][height];
        for (int column = 0; column < width; column++) {
            this.tiles[column] = tiles[column].clone();
        }
        this.levelObjects = Collections.unmodifiableList(
            new ArrayList<>(java.util.Arrays.asList(levelObjects.clone()))
        );
    }

    private static void requireObject(
        LevelObject[] objects,
        LevelObject.Type type,
        int x,
        int y
    ) {
        int matches = 0;
        for (LevelObject object : objects) {
            if (object.getType() == type) {
                matches++;
                if (object.getX() != x || object.getY() != y) {
                    throw new IllegalArgumentException(
                        type + " object position must match its configured level position."
                    );
                }
            }
        }
        if (matches != 1) {
            throw new IllegalArgumentException(
                "Each level must configure exactly one " + type + " object."
            );
        }
    }

    
    public static Tile[][] createOpenTileTable(int width, int height) {

        Tile[][] tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(false, null);
            }
        }
        return tiles;
    }

    
    public int getWidth() {
        return width;
    }
    public void setWalkable(int x, int y, boolean walkable) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Tile position is outside the tile table.");
        }
        tiles[x][y] = new Tile(walkable, null);
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

    public int getTotemX() {
        return totemX;
    }

    public int getTotemY() {
        return totemY;
    }

    public int getEnemyX() {
        return enemyX;
    }

    public int getEnemyY() {
        return enemyY;
    }

    public int getGoldMaxSteps() {
        return goldMaxSteps;
    }

    public int getGoldMaxLines() {
        return goldMaxLines;
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

    public List<LevelObject> getLevelObjects() {
        return levelObjects;
    }

    public LevelObject getBlockingObjectAt(int x, int y) {
        for (LevelObject object : levelObjects) {
            if (object.blocksMovement() && object.getX() == x && object.getY() == y) {
                return object;
            }
        }
        return null;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[x][y];
    }


}
