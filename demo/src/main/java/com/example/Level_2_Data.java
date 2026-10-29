package com.example;

/**
 * Supplies Level 2's values to the shared {@link GameLevelData} structure.
 * Edit these values and {@link #createLevel()} without changing other levels.
 */
public class Level_2_Data {

    // These values belong to Level 2; other levels configure their own coordinates.
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;



    public static final int DOOR_X = 10;
    public static final int DOOR_Y = 9;

    public static final int BUTTON_X = 7;
    public static final int BUTTON_Y = 10;
    
    public static final int HERO_START_X = 1;
    public static final int HERO_START_Y = 1;
    public static final int GOLD_MAX_STEPS = 10;
    public static final int GOLD_MAX_LINES = 10;

    public static final int ENEMY_START_X = 7;
    public static final int ENEMY_START_Y = 6;

    public static final int TOTEM_X = 1;
    public static final int TOTEM_Y = 2;
    
    // Legacy fields retained for compatibility; gameplay state belongs to the mechanics engine.
    public int heroX;
    public int heroY;

    public int totemX;
    public int totemY;

    public int enemyX;
    public int enemyY;
    
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
        // The first index is X (column); the second index is Y (row).
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);
        tiles[BUTTON_X][BUTTON_Y] = new GameLevelData.Tile(
            true,
            "You pressed the switch. The door is now open!"
        );
        tiles[DOOR_X][DOOR_Y] = new GameLevelData.Tile(
            true,
            "You reached the exit."
        );
        tiles[TOTEM_X][TOTEM_Y] = new GameLevelData.Tile(
            false,
            "The totem blocks your path."
        );
        tiles[ENEMY_START_X][ENEMY_START_Y] = new GameLevelData.Tile(
            false,
            "The enemy blocks your path."
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
            TOTEM_X,
            TOTEM_Y,
            ENEMY_START_X,
            ENEMY_START_Y,
            GOLD_MAX_STEPS,
            GOLD_MAX_LINES,
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
            tiles,
            new GameLevelData.LevelObject[] {
                new GameLevelData.LevelObject(
                    GameLevelData.LevelObject.Type.HERO,
                    "/com/example/css/hero.png",
                    null,
                    HERO_START_X,
                    HERO_START_Y,
                    63,
                    63,
                    false,
                    null
                ),
                new GameLevelData.LevelObject(
                    GameLevelData.LevelObject.Type.BUTTON,
                    "/com/example/css/button.png",
                    "/com/example/css/button-pressed.png",
                    BUTTON_X,
                    BUTTON_Y,
                    38,
                    30,
                    false,
                    null
                ),
                new GameLevelData.LevelObject(
                    GameLevelData.LevelObject.Type.DOOR,
                    "/com/example/css/door (2).png",
                    "/com/example/css/door-open.png",
                    DOOR_X,
                    DOOR_Y,
                    66,
                    66,
                    false,
                    null
                ),
                new GameLevelData.LevelObject(
                    GameLevelData.LevelObject.Type.BLOCKER,
                    "/com/example/css/totem.png",
                    null,
                    TOTEM_X,
                    TOTEM_Y,
                    50,
                    50,
                    true,
                    "The totem blocks your path."
                ),
                new GameLevelData.LevelObject(
                    GameLevelData.LevelObject.Type.BLOCKER,
                    "/com/example/css/enemy.png",
                    null,
                    ENEMY_START_X,
                    ENEMY_START_Y,
                    60,
                    60,
                    true,
                    "The enemy blocks your path."
                )
            }
        );
    }

    /** Resets the legacy position fields; the active game engine tracks gameplay movement. */
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;

        totemX = TOTEM_X;
        totemY = TOTEM_Y;

        enemyX = ENEMY_START_X;
        enemyY = ENEMY_START_Y;
        
    }
}
