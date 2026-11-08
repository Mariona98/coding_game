package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Supplies Level 3's map and randomized enemy positions. */
public class Level_3_Data {

    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;
    public static final int HERO_START_X = 3;
    public static final int HERO_START_Y = 5;
    public static final int ENEMY_COUNT = 6;
    public static final int GOLD_MAX_STEPS = 10;
    public static final int GOLD_MAX_LINES = 10;
    public static final int COMMAND_USAGE_LIMIT = 5;
    // The sample solver may cross the 12x12 map to reach every randomized enemy.
    public static final int MOVEMENT_COMMAND_USAGE_LIMIT =
        ENEMY_COUNT * (LEVEL_WIDTH + LEVEL_HEIGHT - 2);
    // One attack is needed to remove each of the six enemies.
    public static final int ATTACK_COMMAND_USAGE_LIMIT = ENEMY_COUNT;

    public int heroX;
    public int heroY;

    public static final String[] DIALOGUES = {
        "Welcome, hero!",
        "Explore the 12 by 12 map.",
        "Six enemies are scattered across the map.",
        "Scan for their positions, then defeat them all!"
    };

    public static final GameLevelData LEVEL = createLevel();

    public Level_3_Data() {
        resetPositions();
    }

    /** Creates a fresh level so each run gets a new enemy layout. */
    public static GameLevelData createLevel() {
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);
        for (int x = 0; x < LEVEL_WIDTH; x++) {
            for (int y = 0; y < LEVEL_HEIGHT; y++) {
                tiles[x][y] = new GameLevelData.Tile(true, null);
            }
        }

        List<int[]> availablePositions = new ArrayList<>();
        for (int y = 0; y < LEVEL_HEIGHT; y++) {
            for (int x = 0; x < LEVEL_WIDTH; x++) {
                if (x != HERO_START_X || y != HERO_START_Y) {
                    availablePositions.add(new int[] {x, y});
                }
            }
        }
        Collections.shuffle(availablePositions);

        GameLevelData.LevelObject[] objects =
            new GameLevelData.LevelObject[ENEMY_COUNT + 1];
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
            int[] position = availablePositions.get(i);
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

        return new GameLevelData(
            LEVEL_WIDTH,
            LEVEL_HEIGHT,
            GOLD_MAX_STEPS,
            GOLD_MAX_LINES,
            "/com/example/css/level3.png",
            "The hero cannot move beyond the edge of this map.",
            DIALOGUES,
            "Explore the map while avoiding the six enemies.",
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
                    "Level 3 objective",
                    "Scan the map, move next to an enemy, and use hero.attack(). "
                        + "Defeat all six enemies to win.",
                    "enemy_count, enemies = hero.scan()\nhero.attack()"
                ),
                new GameLevelData.HelpSection(
                    "Scan for enemies",
                    "hero.scan() returns the enemy count and their (x, y) coordinates.",
                    "enemy_count, enemy_positions = hero.scan()\n"
                        + "print(enemy_count)\n"
                        + "for x, y in enemy_positions:\n"
                        + "    print(x, y)"
                ),
                new GameLevelData.HelpSection(
                    "Command limits",
                    "You can use up to " + MOVEMENT_COMMAND_USAGE_LIMIT
                        + " movement commands total and " + ATTACK_COMMAND_USAGE_LIMIT
                        + " attacks. Scan, Add, and Drop each have a limit of "
                        + COMMAND_USAGE_LIMIT + ".",
                    "hero.moveRight()\nhero.moveUp()"
                )
            },
            tiles,
            objects,
            COMMAND_USAGE_LIMIT,
            MOVEMENT_COMMAND_USAGE_LIMIT,
            ATTACK_COMMAND_USAGE_LIMIT
        );
    }

    /** Resets the legacy position fields; the active game engine tracks gameplay movement. */
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
    }
}
