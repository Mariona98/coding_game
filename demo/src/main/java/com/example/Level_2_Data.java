package com.example;

/**
 * Supplies Level 1's values to the shared {@link GameLevelData} structure.
 * Edit these values and {@link #createLevel()} without changing other levels.
 */
public class Level_2_Data {

    // These values belong to Level 1; other levels configure their own coordinates.
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;

    
    public static final int HERO_START_X = 3;
    public static final int HERO_START_Y = 5;
    public static final int GOLD_MAX_STEPS = 10;
    public static final int GOLD_MAX_LINES = 10;
    public static final int COMMAND_USAGE_LIMIT = 15;

    // Legacy fields retained for compatibility; gameplay state belongs to the mechanics engine.
    public int heroX;
    public int heroY;
    
    public static final String[] DIALOGUES = {
        "It seems there is no door here ...",
        "Maybe this ritualistic symbols will open a protal to the next level.",
        "I could activate them if i draw them with the paint ",
        "Help me carry the necessary amount of paint to the symbols and draw them."
    };

    public static final GameLevelData LEVEL = createLevel();

    /** Initializes the legacy position fields to Level 1's configured start. */
    public Level_2_Data() {
        resetPositions();
    }

    /** Builds Level 1's tile map and combines all of its values into shared level data. */
    private static GameLevelData createLevel() {
        // The first index is X (column); the second index is Y (row).
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);



        for (int y = 0; y <= 11; y++) {
            for (int x = 0; x <= 11; x++) {
                tiles[x][y] = new GameLevelData.Tile(true, null);
            }
        }
       
        System.out.println("The walkable tiles are:");  
        for (int y = 0; y < LEVEL_HEIGHT; y++) {
            for (int x = 0; x < LEVEL_WIDTH; x++) {
                if (tiles[x][y].isWalkable()) {
                    System.out.println("  (" + x + ", " + y + ")");
                }
            }
        }

        return new GameLevelData(

            LEVEL_WIDTH,
            LEVEL_HEIGHT,
            GOLD_MAX_STEPS,
            GOLD_MAX_LINES,
            "/com/example/css/level2.png",
            "The hero cannot move beyond the edge of this map.",
            DIALOGUES,
            "Use movement commands to find the switch, open the door, and reach the exit.",
            new GameLevelData.HelpSection[] {
                
                new GameLevelData.HelpSection(
                    "Import the hero",
                    "Add this import before issuing movement commands.",
                    "from game_api import hero"
                ),
                new GameLevelData.HelpSection(
                    "Move one tile",
                    "Each method moves the hero one tile in the named direction.",
                    "hero.moveRight()\nhero.moveLeft()\nhero.moveUp()\nhero.moveDown()"
                ),
                new GameLevelData.HelpSection(
                    "Add and remove paint from the hero",
                    "The hero can carry paint and use it to draw on the floor. Use the addPaint method to add paint to the hero's inventory, and use the removePaint method to remove paint from the hero's inventory.",
                    "hero.addPaint()\nhero.removePaint()"
                ),
                new GameLevelData.HelpSection(
                    "Level 2 objective",
                    "Complete the ritual by drawing the symbols on the floor using the paint.",
                    "null"
                ),
                new GameLevelData.HelpSection(
                    "Gold objectives",
                    "The hero that can complete the level in fewer than " + GOLD_MAX_STEPS + " steps and fewer than " + GOLD_MAX_LINES + " lines of code will be awarded gold.",
                    null)
                

            },

            tiles,
            new GameLevelData.LevelObject[] {
                
                new GameLevelData.LevelObject(
                    "hero",
                    GameLevelData.LevelObject.Type.HERO,
                    "/com/example/css/hero.png",
                    null,
                    HERO_START_X,
                    HERO_START_Y,
                    66,
                    66,
                    false,
                    null,
                    false

                ),
                new GameLevelData.LevelObject(
                    "RED",
                    GameLevelData.LevelObject.Type.PUZZLE,
                    "/com/example/css/red symbol.png",
                    "/com/example/css/red symbol activated.png",
                    5,
                    5,
                    66,
                    66,
                    false,
                    null,
                    false
                ),
                new GameLevelData.LevelObject(
                    "RED",
                    GameLevelData.LevelObject.Type.PUZZLE,
                    "/com/example/css/red symbol.png",
                    "/com/example/css/red symbol activated.png",
                    5,
                    6,
                    66,
                    66,
                    false,
                    null,
                    false
                ),
                new GameLevelData.LevelObject(
                    "RED",
                    GameLevelData.LevelObject.Type.ITEM,
                    "/com/example/css/redpaint.png",
                    null,
                    8,
                    5,
                    66,
                    66,
                    false,
                    null,
                    false
                )




             
            },
            COMMAND_USAGE_LIMIT,
            COMMAND_USAGE_LIMIT,
            COMMAND_USAGE_LIMIT,
            GameLevelData.WinCondition.ACTIVATE_ALL_PUZZLES
        );
    }

    /** Resets the legacy position fields; the active game engine tracks gameplay movement. */
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }



    
}
