package com.example;
 
public class Level_1_Data {

    // Fixed Level 1 map coordinates.
    public static final int LEVEL_WIDTH = 10;
    public static final int LEVEL_HEIGHT = 9;



    public static final int DOOR_X = 7;
    public static final int DOOR_Y = 5;

    public static final int BUTTON_X = 1;
    public static final int BUTTON_Y = 1;
    
    public static final int HERO_START_X = 1;
    public static final int HERO_START_Y = 6;
    

    public int heroX;
    public int heroY;
    

    public Level_1_Data() {
        resetPositions();
    }

    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }
}
