package com.example;


public class Level_2_Data {

    
    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;

    
    public static final int HERO_START_X = 1;
    public static final int HERO_START_Y = 9;
    public static final int GOLD_MAX_STEPS = 22;
    public static final int GOLD_MAX_LINES = 20;
    public static final int COMMAND_USAGE_LIMIT = 15;

    //lines basic 33
    //steps basic 30

    
    public int heroX;
    public int heroY;
    
    public static final String[] DIALOGUES = {
        "It seems there is no door here ...",
        "Maybe this ritualistic symbols will open a protal to the next level.",
        "I could activate them if i draw them with the paint ",
        "Help me carry the necessary amount of paint to the symbols and draw them."
    };

    public static final GameLevelData LEVEL = createLevel();

    
    public Level_2_Data() {
        resetPositions();
    }

    
    private static GameLevelData createLevel() {
        // The first index is X (column) the second index is Y (row).
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);



        for (int y = 0; y <= 11; y++) {
            for (int x = 0; x <= 11; x++) {
                tiles[x][y] = new GameLevelData.Tile(true);
            }
        }

        int[][] puzzlePositions = {
            {6, 3},
            {7, 3},
            {6, 4},
            {7, 4},
            {6, 6},
            {7, 6},
            {6,7},
            {7,7},
            {6,8},
            {5,9}
        };
       
        System.out.println("The walkable tiles are:");  
        for (int y = 0; y < LEVEL_HEIGHT; y++) {
            for (int x = 0; x < LEVEL_WIDTH; x++) {
                if (tiles[x][y].isWalkable()) {
                    System.out.println("  (" + x + ", " + y + ")");
                }
            }
        }




        GameLevelData.LevelObject[] objects =
            new GameLevelData.LevelObject[12];
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

        for (int i = 0; i < 10; i++) {
            int[] position = puzzlePositions[i];
            objects[i + 1] = new GameLevelData.LevelObject(
                "RED",
                GameLevelData.LevelObject.Type.PUZZLE,
                "/com/example/css/red symbol.png",
                "/com/example/css/red symbol activated.png",
                position[0],
                position[1],
                52,
                52,
                false,
                null,
                false
            );
        }
        objects[11] = new GameLevelData.LevelObject(
            "RED",
            GameLevelData.LevelObject.Type.ITEM,
            "/com/example/css/redpaint.png",
            null,
            3,
            5,
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
            "/com/example/css/level2.png",
            "The hero cannot move beyond the edge of this map.",
            DIALOGUES,
            "Use movement commands to find the switch, open the door, and reach the exit.",
            new GameLevelData.HelpSection[] {
                
                new GameLevelData.HelpSection(
                    "Import the hero",
                    "Import the hero and inventory before issuing commands.",
                    "from game_api import hero, inventory"
                ),
                new GameLevelData.HelpSection(
                    "Move one tile",
                    "Each method moves the hero one tile in the named direction.",
                    "hero.moveRight()\nhero.moveLeft()\nhero.moveUp()\nhero.moveDown()"
                ),
                new GameLevelData.HelpSection(
                    "Add and remove paint from the hero",
                    "Use inventory.append() while on the paint item to pick it up. "
                        + "Use inventory.remove() while on a symbol to drop paint "
                        + "and activate that symbol.",
                    "inventory.append()\ninventory.remove()"
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
            objects,
            COMMAND_USAGE_LIMIT,
            COMMAND_USAGE_LIMIT,
            COMMAND_USAGE_LIMIT,
            GameLevelData.WinCondition.ACTIVATE_ALL_PUZZLES
        );
    }

    
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }



    
}
