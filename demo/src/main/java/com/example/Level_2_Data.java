package com.example;
 
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


    public Level_2_Data() {
        resetPositions();
    }

    public void resetPositions() {
        heroX = HERO_START_X;
        heroY = HERO_START_Y;
        
    }
}
