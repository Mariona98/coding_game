package com.example;

/**
 * Supplies Level 2's values to the shared {@link GameLevelData} structure.
 * Edit these values and {@link #createLevel()} without changing other levels.
 */
public class Level_2_Data {

    // These values belong to Level 2; other levels configure their own coordinates.
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;



    public static final int DOOR_X = 5;
    public static final int DOOR_Y = 7;

    public static final int BUTTON_X = 5;
    public static final int BUTTON_Y = 6;
    
    public static final int HERO_START_X = 5;
    public static final int HERO_START_Y = 5;

    // Legacy fields retained for compatibility; gameplay state belongs to the mechanics engine.
    public int heroX;
    public int heroY;
    
    public static final String[] DIALOGUES = {
        "Welcome, hero!",
        "You need to reach the door.",
        "But the door is currently closed.",
        "Find the button and press it.",
        "Then move to the door.",
        "Good luck!"
    };

    public static final GameLevelData LEVEL = createLevel();

    /** Initializes the legacy position fields to Level 2's configured start. */
    public Level_2_Data() {
        resetPositions();
    }

    /** Builds Level 2's tile map and combines all of its values into shared level data. */
    private static GameLevelData createLevel() {
        // Rows are Y coordinates and columns are X coordinates.
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);
        tiles[BUTTON_Y][BUTTON_X] = new GameLevelData.Tile(
            true,
            "You pressed the switch. The door is now open!"
        );
        tiles[DOOR_Y][DOOR_X] = new GameLevelData.Tile(
            true,
            "You reached the exit."
        );

        return new GameLevelData(
            LEVEL_WIDTH,
            LEVEL_HEIGHT,
            HERO_START_X,
            HERO_START_Y,
            BUTTON_X,
            BUTTON_Y,
            DOOR_X,
            DOOR_Y,
            "/com/example/css/level2.png",
            "The hero cannot move beyond the edge of this map.",
            DIALOGUES,
            "Use the same hero movement commands to reach the nearby switch and exit.",
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
                    "Level 2 objective",
                    "The switch and exit are directly below the hero. Press the switch, "
                        + "then continue to the exit.",
                    "hero.moveDown()\nhero.moveDown()"
                )
            },
            tiles
        );
    }

    /** Resets the legacy position fields; the active game engine tracks gameplay movement. */
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }
}
