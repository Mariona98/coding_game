package com.example;

/**
 * Defines Level 2's dimensions, object locations, dialogue, background, and
 * tile messages. Update {@link #createLevel()} to change this level's map.
 */
public class Level_2_Data {

    // Fixed Level 2 map coordinates.
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;



    public static final int DOOR_X = 5;
    public static final int DOOR_Y = 7;

    public static final int BUTTON_X = 5;
    public static final int BUTTON_Y = 6;
    
    public static final int HERO_START_X = 5;
    public static final int HERO_START_Y = 5;

    // Legacy position fields; active gameplay position is held by Level_1_Mechanics.
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


    public Level_2_Data() {
        resetPositions();
    }

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

    public void resetPositions() {
        // Kept for callers that still use this data holder's legacy position fields.
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }
}
