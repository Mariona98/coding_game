package com.example;

/**
 * Supplies Level 1's values to the shared {@link GameLevelData} structure.
 * Edit these values and {@link #createLevel()} without changing other levels.
 */
public class Level_1_Data {

    // These values belong to Level 1; other levels configure their own coordinates.
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;



    public static final int DOOR_X = 8;
    public static final int DOOR_Y = 5;

    public static final int BUTTON_X = 5;
    public static final int BUTTON_Y = 3;
    
    public static final int HERO_START_X = 3;
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

    /** Initializes the legacy position fields to Level 1's configured start. */
    public Level_1_Data() {
        resetPositions();
    }

    /** Builds Level 1's tile map and combines all of its values into shared level data. */
    private static GameLevelData createLevel() {
        // Rows are Y coordinates and columns are X coordinates.
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);



        for (int y=4;y<=7;y++)
            for (int x=4;x<=8;x++)
                tiles[x][y] = new GameLevelData.Tile(true, null);
        for (int y=4;y<=6;y++)
            tiles[8][y] = new GameLevelData.Tile(true, null);
        for (int y=4;y<=6;y++)
            tiles[3][y] = new GameLevelData.Tile(true, null);
        System.out.println("The walkable tiles are:");  
        for (int y=0;y<LEVEL_HEIGHT;y++) {
            for (int x=0;x<LEVEL_WIDTH;x++) {
                if (tiles[x][y].isWalkable()) {
                    System.out.println("  (" + x + ", " + y + ")");
                }
            }
        }

        return new GameLevelData(
            LEVEL_WIDTH,
            LEVEL_HEIGHT,
            HERO_START_X,
            HERO_START_Y,
            BUTTON_X,
            BUTTON_Y,
            DOOR_X,
            DOOR_Y,
            "/com/example/css/level1.png",
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
                    "Level 1 objective",
                    "The switch is two tiles to the right and two tiles up from the starting point. "
                        + "Press it, then make your way to the exit.",
                    "hero.moveRight()\nhero.moveUp()"
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
