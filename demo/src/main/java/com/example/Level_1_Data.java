package com.example;


public class Level_1_Data {

    public static final int LEVEL_WIDTH = 12;
    public static final int LEVEL_HEIGHT = 12;

    



    public static final int DOOR_X = 8;
    public static final int DOOR_Y = 5;

    public static final int BUTTON_X = 5;
    public static final int BUTTON_Y = 3;

    public static final int DECOY_X = 5;
    public static final int DECOY_Y = 4;
    
    public static final int HERO_START_X = 3;
    public static final int HERO_START_Y = 5;

    public static final int GOLD_MAX_STEPS = 9;
    public static final int GOLD_MAX_LINES = 11;

    public static final int COMMAND_USAGE_LIMIT = 16;//lets say lines

    // Level 1 needs at least nine moves to press the button and reach the door.
    public static final int MOVEMENT_COMMAND_USAGE_LIMIT = 10;//to pass

    // Legacy fields retained for compatibility; gameplay state belongs to the mechanics engine.
    public int heroX;
    public int heroY;
    
    public static final String[] DIALOGUES = {
        "Oh hello , could you help me out? I need to get to the door but it is locked.",
        "I think one of the buttons will open the door for me.",
        "It seems one of them is a decoy, I need  to get closer to check which one is the real button.",
        "You can use the movement commands to help me navigate the map , and i will tell you if i see the DECOY.",
        "Awaiting your commands!"
    };

    public static final GameLevelData LEVEL = createLevel();

   
    public Level_1_Data() {
        resetPositions();
    }

    
    private static GameLevelData createLevel() {
        // The first index is X (column) the second index is Y (row).
        GameLevelData.Tile[][] tiles =
            GameLevelData.createOpenTileTable(LEVEL_WIDTH, LEVEL_HEIGHT);



        for (int y = 4; y <= 6; y++) {
            for (int x = 3; x <= 8; x++) {
                tiles[x][y] = new GameLevelData.Tile(true);
            }
        }
        for (int x = 4; x <= 7; x++) {
            tiles[x][3] = new GameLevelData.Tile(true);
            tiles[x][7] = new GameLevelData.Tile(true);
        }
        tiles[BUTTON_X][BUTTON_Y] = new GameLevelData.Tile(true);
        tiles[DOOR_X][DOOR_Y] = new GameLevelData.Tile(true);
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
                    "Getting the Id of the objects",
                    "You can get the Id of the object in a specific direction by using the getObjectId method which returns the id ( string ) of the object in that direction.",
                    "hero.getObjectId(\"RIGHT\")\nhero.getObjectId(\"LEFT\")\nhero.getObjectId(\"UP\")\nhero.getObjectId(\"DOWN\")"
                ),
                new GameLevelData.HelpSection(
                    "Level 1 objective",
                    "Press the button and avoid the decoy to open the door and reach the exit."
                        + "Press it, then make your way to the exit.",
                        null
                    
                ),
                new GameLevelData.HelpSection(
                    "Command limits",
                    "You can use up to " + MOVEMENT_COMMAND_USAGE_LIMIT
                        + " movement commands total (all directions combined) and up to "
                        + COMMAND_USAGE_LIMIT + " uses of each other command.",
                        null
                    
                )
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
                    "DECOY",
                    GameLevelData.LevelObject.Type.ENEMY,
                    "/com/example/css/button.png",
                    null,
                    DECOY_X,
                    DECOY_Y,
                    66,
                    66,
                    false,
                    null,
                    false
                ),
                new GameLevelData.LevelObject(
                    "button",
                    GameLevelData.LevelObject.Type.BUTTON,
                    "/com/example/css/button.png",
                    null,
                    BUTTON_X,
                    BUTTON_Y,
                    66,
                    66,
                    false,
                    null,
                    false
                ),
                
                new GameLevelData.LevelObject(
                    "door",
                    GameLevelData.LevelObject.Type.DOOR,
                    "/com/example/css/door (2).png",
                    null,
                    DOOR_X,
                    DOOR_Y,
                    66,
                    66,
                    false,
                    null,
                    false
                )
             
            },
            COMMAND_USAGE_LIMIT,
            MOVEMENT_COMMAND_USAGE_LIMIT,
            COMMAND_USAGE_LIMIT,
            GameLevelData.WinCondition.REACH_OPEN_DOOR
        );
    }

    
    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }



    
}
