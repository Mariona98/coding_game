package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameLevelData {

    //The goals available for levels
    public enum WinCondition {
        REACH_OPEN_DOOR,
        ACTIVATE_ALL_PUZZLES,
        DEFEAT_ALL_ENEMIES
    }

    //maybe for later uses
    public static final int DEFAULT_COMMAND_USAGE_LIMIT = 5;

    
    public static final class LevelObject {

        //type of objects available in the levels
        public enum Type {
            HERO,
            BUTTON,
            DOOR,
            PUZZLE,
            ITEM,
            ENEMY,
            TOTEM,
            DECOR
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
        private final String id;
        private final boolean initiallyActivated;
        private boolean activated;
  
     
        public LevelObject(
            String id,
            Type type,
            String imagePath,
            String activatedImagePath,
            int x,
            int y,
            double fitWidth,
            double fitHeight,
            boolean blocksMovement,
            String blockedMessage,
            boolean activated
        ) {
            if (type == null || imagePath == null || imagePath.isEmpty()) {
                throw new IllegalArgumentException("Level objects need a type and image path.");
            }
            if (x < 0 || y < 0 || fitWidth <= 0 || fitHeight <= 0) {
                throw new IllegalArgumentException("Level object position and size must be positive.");
            }
            this.id = id;
            this.type = type;
            this.imagePath = imagePath;
            this.activatedImagePath = activatedImagePath;
            this.x = x;
            this.y = y;
            this.fitWidth = fitWidth;
            this.fitHeight = fitHeight;
            this.blocksMovement = blocksMovement;
            this.blockedMessage = blockedMessage;
            this.activated = activated;
            this.initiallyActivated = activated;
        }

        public Type getType() {
            return type;
        }

        
        public String getId() {
            return id;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getActivatedImagePath() {
            return activatedImagePath;
        }

        public boolean isActivated() {
            return activated;
        }

        private void resetActivation() {
            activated = initiallyActivated;
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

    
    public static final class Tile {
        private final boolean walkable;
   

        
        public Tile(boolean walkable) {
            this.walkable = walkable;
            
        }

        
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
    private final int goldMaxSteps;
    private final int goldMaxLines;
    private final String backgroundPath;
    private final String boundaryMessage;
    private final String[] dialogues;
    private final String commandHelpIntroduction;
    private final HelpSection[] commandHelpSections;
    private final Tile[][] tiles;
    private final List<LevelObject> levelObjects;
    private final int commandUsageLimit;
    private final int movementCommandUsageLimit;
    private final int attackCommandUsageLimit;
    private final WinCondition winCondition;


    //creates a level
    public GameLevelData(
        int width,
        int height,
        int goldMaxSteps,
        int goldMaxLines,
        String backgroundPath,
        String boundaryMessage,
        String[] dialogues,
        String commandHelpIntroduction,
        HelpSection[] commandHelpSections,
        Tile[][] tiles,
        LevelObject[] levelObjects,
        int commandUsageLimit,
        int movementCommandUsageLimit,
        int attackCommandUsageLimit,
        WinCondition winCondition
    ) {
        if (width <= 0 || height <= 0 || tiles == null || tiles.length != width) {
            throw new IllegalArgumentException("Tile table dimensions do not match the level size.");
        }
        if (goldMaxSteps < 0
            || goldMaxLines < 0
            || commandUsageLimit < 0
            || movementCommandUsageLimit < 0
            || attackCommandUsageLimit < 0) {
            throw new IllegalArgumentException(
                "Gold score targets and command usage limit cannot be negative."
            );
        }
        if (dialogues == null
            || commandHelpIntroduction == null
            || commandHelpSections == null
            || levelObjects == null) {
            throw new IllegalArgumentException("Level dialogue and command help must be configured.");
        }
        if (winCondition == null) {
            throw new IllegalArgumentException("Each level must configure a win condition.");
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


        this.width = width;
        this.height = height;
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
        // Keep a mutable internal list so mechanics can remove defeated enemies.
        // getLevelObjects() still exposes a read-only view to other components.
        this.levelObjects = new ArrayList<>(
            java.util.Arrays.asList(levelObjects.clone())
        );
        this.commandUsageLimit = commandUsageLimit;
        this.movementCommandUsageLimit = movementCommandUsageLimit;
        this.attackCommandUsageLimit = attackCommandUsageLimit;
        this.winCondition = winCondition;
    }

   
    public static Tile[][] createOpenTileTable(int width, int height) {

        Tile[][] tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(false);
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
        tiles[x][y] = new Tile(walkable);
    }

   
    public int getHeight() {
        return height;
    }

    

    
 
    public int getGoldMaxSteps() {
        return goldMaxSteps;
    }

    public int getGoldMaxLines() {
        return goldMaxLines;
    }

    //later use
    public int getCommandUsageLimit() {
        return commandUsageLimit;
    }

    //later use
    public int getMovementCommandUsageLimit() {
        return movementCommandUsageLimit;
    }

    //later use
    public int getAttackCommandUsageLimit() {
        return attackCommandUsageLimit;
    }

    
    public WinCondition getWinCondition() {
        return winCondition;
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
        return Collections.unmodifiableList(levelObjects);
    }

  


public int getButtonObjectX() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.BUTTON) {
                return object.getX();
            }
        }
        return -1; 
    }
    public int getButtonObjectY() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.BUTTON) {
                return object.getY();
            }
        }
        return -1; 
    }

    public int getHeroObjectX() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.HERO) {
                return object.getX();
            }
        }
        return -1; 
    }
    public int getHeroObjectY() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.HERO) {
                return object.getY();
            }
        }
        return -1; 
    }
    public int getItemObjectX() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.ITEM) {
                return object.getX();
            }
        }
        return -1; 
    }
    public int getItemObjectY() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.ITEM) {
                return object.getY();
            }
        }
        return -1; 
    }
    public int getDoorX() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.DOOR) {
                return object.getX();
            }
        }
        return -1; 
    }
    public int getDoorY() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.DOOR) {
                return object.getY();
            }
        }
        return -1; 
    }
    public LevelObject getLevelObjectById(String id) {
        for (LevelObject object : levelObjects) {
            if (object.id.equals(id)) {
                return object;
            }
        }
        return null;
    }

    public String getLevelObjectId(int x, int y) {
        for (LevelObject object : levelObjects) {
            if ((object.getType() == LevelObject.Type.ITEM
                    || object.getType() == LevelObject.Type.PUZZLE)
                && object.getX() == x
                && object.getY() == y) {
                return object.id;
            }
        }
        return null; 
    }

    public String getPuzzleObjectIdAt(int x, int y) {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.PUZZLE
                && object.getX() == x
                && object.getY() == y) {
                return object.id;
            }
        }
        return null;
    }

    public String getAddItemId(int x, int y) {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.ITEM
                && object.getX() == x
                && object.getY() == y) {
                return object.id;
            }
        }
        return null;
    }

        public int getPuzzleObjectX() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.PUZZLE) {
                return object.getX();
            }
        }
        return -1; 
    }
    public int getPuzzleObjectY() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.PUZZLE) {
                return object.getY();
            }
        }
        return -1; 
    }
    
    public void setLevelObjectActivatedAt(
        LevelObject.Type type,
        int x,
        int y,
        boolean activated
    ) {
        for (LevelObject object : levelObjects) {
            if (object.getType() == type
                && object.getX() == x
                && object.getY() == y) {
                object.activated = activated;
                return;
            }
        }
    }

    public void setLevelObjectsActivated(LevelObject.Type type, boolean activated) {
        for (LevelObject object : levelObjects) {
            if (object.getType() == type) {
                object.activated = activated;
            }
        }
    }

    public void resetLevelObjectActivations() {
        for (LevelObject object : levelObjects) {
            object.resetActivation();
        }
    }
    
   public String getHeroObjectImagePath() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.HERO) {
                return object.getImagePath();
            }
        }
        return null; 
    }
    public int getHeroObjectFitWidth() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.HERO) {
                return (int) object.getFitWidth();
            }
        }
        return -1;
    }
    public int getHeroObjectFitHeight() {
        for (LevelObject object : levelObjects) {
            if (object.getType() == LevelObject.Type.HERO) {
                return (int) object.getFitHeight();
            }
        }
        return -1; 
    } 

   
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return tiles[x][y];
    }

    public void removeLevelObject(LevelObject object) {
        levelObjects.remove(object);
    }

}
