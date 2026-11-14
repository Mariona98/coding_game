package com.example;

/** Supplies Level 3's lava map, fixed enemies, and programmable totem. */
public class Level_3_Data {

    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;
    public static final int HERO_START_X = 1;
    public static final int HERO_START_Y = 1;
    public static final int TOTEM_START_X = 1;
    public static final int TOTEM_START_Y = 2;
    public static final int ENEMY_COUNT = 6;
    public static final int GOLD_MAX_STEPS = 25;
    public static final int GOLD_MAX_LINES = 30;
    public static final int COMMAND_USAGE_LIMIT = 15;
    public static final int MOVEMENT_COMMAND_USAGE_LIMIT = 20;
    // One attack is needed to remove each of the six enemies.
    public static final int ATTACK_COMMAND_USAGE_LIMIT = ENEMY_COUNT;

    public int heroX;
    public int heroY;

    public static final String[] DIALOGUES = {
        "Welcome, hero!",
        "The hero and totem are trapped on two safe tiles.",
        "Program the totem with a def function to cross the lava.",
        "Defeat all six enemies to complete the level."
    };

    public static final GameLevelData LEVEL = createLevel();

    public Level_3_Data() {
        resetPositions();
    }

    /** Creates the level with the same enemy layout on every run. */
    public static GameLevelData createLevel() {
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);
        tiles[HERO_START_X][HERO_START_Y] =
            new GameLevelData.Tile(true, null);
        tiles[TOTEM_START_X][TOTEM_START_Y] =
            new GameLevelData.Tile(true, null);

        int[][] enemyPositions = {
            {2, 2},
            {4, 2},
            {7, 2},
            {10, 2},
            {10, 4},
            {5, 4}
        };
        GameLevelData.LevelObject[] objects =
            new GameLevelData.LevelObject[ENEMY_COUNT + 2];
        objects[0] = new GameLevelData.LevelObject(
            "hero",
            GameLevelData.LevelObject.Type.HERO,
            "/com/example/css/hero.png",
            null,
            HERO_START_X,
            HERO_START_Y,
            63,
            63,
            false,
            null,
            false
        );

        for (int i = 0; i < ENEMY_COUNT; i++) {
            int[] position = enemyPositions[i];
            objects[i + 1] = new GameLevelData.LevelObject(
                "enemy-" + (i + 1),
                GameLevelData.LevelObject.Type.ENEMY,
                "/com/example/css/enemy.png",
                null,
                position[0],
                position[1],
                52,
                52,
                false,
                null,
                false
            );
        }
        objects[ENEMY_COUNT + 1] = new GameLevelData.LevelObject(
            "function-totem",
            GameLevelData.LevelObject.Type.TOTEM,
            "/com/example/css/totem.png",
            null,
            TOTEM_START_X,
            TOTEM_START_Y,
            52,
            52,
            false,
            null,
            false
        );

        return new GameLevelData(
            LEVEL_WIDTH,
            LEVEL_HEIGHT,
            GOLD_MAX_STEPS,
            GOLD_MAX_LINES,
            "/com/example/Lava #4.png",
            "The hero cannot leave the two safe tiles.",
            DIALOGUES,
            "Define a function that programs the totem to defeat all six enemies.",
            new GameLevelData.HelpSection[] {
                new GameLevelData.HelpSection(
                    "Import the totem",
                    "The hero stays safe while the totem crosses the lava.",
                    "from game_api import totem"
                ),
                new GameLevelData.HelpSection(
                    "Program the totem",
                    "Define a function with def, then have the adjacent hero call "
                        + "hero.command(function_name). The totem only accepts "
                        + "commands issued inside that function.",
                    "def defeat_enemies():\n"
                        + "    totem.moveRight()\n"
                        + "    totem.attack()\n\n"
                        + "hero.command(defeat_enemies)"
                ),
                new GameLevelData.HelpSection(
                    "Level 3 objective",
                    "Only the totem can walk on lava. It must stand next to an enemy "
                        + "to attack. Defeat all six enemies to win.",
                    "totem.moveRight()\ntotem.attack()"
                ),
                new GameLevelData.HelpSection(
                    "Gold objectives",
                    "The hero that can complete the level in fewer than " + GOLD_MAX_STEPS + " steps and fewer than " + GOLD_MAX_LINES + " lines of code will be awarded gold.",
                    null
                )   
                
            },
            tiles,
            objects,
            COMMAND_USAGE_LIMIT,
            MOVEMENT_COMMAND_USAGE_LIMIT,
            ATTACK_COMMAND_USAGE_LIMIT,
            GameLevelData.WinCondition.DEFEAT_ALL_ENEMIES
        );
    }

    /** Resets the legacy position fields; the active game engine tracks gameplay movement. */
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
    }
}
