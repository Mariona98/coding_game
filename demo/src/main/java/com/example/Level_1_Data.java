package com.example;
 
public class Level_1_Data {

    // Fixed Level 1 map coordinates.
    public static final int LEVEL_WIDTH = 10;
    public static final int LEVEL_HEIGHT = 9;

    public static final int TOWER_1_X = 5;
    public static final int TOWER_1_Y = 4;
    public static final int TOWER_2_X = 5;
    public static final int TOWER_2_Y = 6;

    public static final int BOW_X = 0;
    public static final int BOW_Y = 2;

    public static final int CHEST_1_X = 8;
    public static final int CHEST_1_Y = 1;
    public static final int CHEST_2_X = 8;
    public static final int CHEST_2_Y = 2;

    public static final int DOOR_X = 7;
    public static final int DOOR_Y = 5;

    // Starting positions of moving objects.
    public static final int HERO_START_X = 1;
    public static final int HERO_START_Y = 6;
    public static final int BUG_1_START_X = 6;
    public static final int BUG_1_START_Y = 1;
    public static final int BUG_2_START_X = 6;
    public static final int BUG_2_START_Y = 2;

    public int heroX;
    public int heroY;
    public int bug1X;
    public int bug1Y;
    public int bug2X;
    public int bug2Y;

    public Level_1_Data() {
        resetPositions();
    }

    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        bug1X = BUG_1_START_X;
        bug1Y = BUG_1_START_Y;
        bug2X = BUG_2_START_X;
        bug2Y = BUG_2_START_Y;
    }
}
