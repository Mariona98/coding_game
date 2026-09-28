package com.example;

import java.util.HashSet;
import java.util.Set;

public class Level_1_Mechanics {

    private int heroX;
    private int heroY;

    


public boolean dooropened = false;
    public boolean GameOver = false;

    public boolean pressButton = false;
    // Level 1-only temporary and hazard state.
  

    private int resetTimes = 0;
    private long levelStartTime;

   

    public Level_1_Mechanics(Level_1_Data level) {
        heroX = level.heroX;
        heroY = level.heroY;
     

        levelStartTime = System.currentTimeMillis();
        System.out.println("Level 1 started.");
    }

    public void executeCommand(Level_1_Commands cmd) {
        if (GameOver) {
            System.out.println("Command ignored because the game is over.");
            return;
        }
        if (cmd == null) {
            System.out.println("No command was provided.");
            return;
        }

        int oldX = heroX;
        int oldY = heroY;
        boolean moved = false;

        switch (cmd) {
            case MOVE_RIGHT:
                heroX++;
                moved = true;
                break;

            case MOVE_LEFT:
                heroX--;
                moved = true;
                break;

            case MOVE_UP:
                heroY--;
                moved = true;
                break;

            case MOVE_DOWN:
                heroY++;
                moved = true;
                break;

            default:
                System.out.println("Unsupported Level 1 command.");
        }

        if (!moved) {
            return;
        }

        

        System.out.println("Hero moved to (" + heroX + ", " + heroY + ").");
       
    }
    public void updateButtonState() {
        if (heroX == Level_1_Data.BUTTON_X && heroY == Level_1_Data.BUTTON_Y) {
            pressButton = true;
            this.dooropened = true;
            System.out.println("Button pressed!");
        } else if (dooropened) {
            System.out.println("Door is open.");
            
        }
        else{
            pressButton = false;
            this.dooropened = false;
        }
    }
  
    public boolean isDoorOpen() {
       
        return this.dooropened;
    }
    public boolean checkWin() {
        if (isDoorOpen() && heroX == Level_1_Data.DOOR_X && heroY == Level_1_Data.DOOR_Y) {
            System.out.println("Hero reached the open door. Level completed!");
            return true;
           
        }
        return false;
    }

    public boolean checkHeroDead() {
        return GameOver;
    }




    public void resetGame() {
        resetTimes++;

        heroX = Level_1_Data.HERO_START_X;
        heroY = Level_1_Data.HERO_START_Y;

    
        GameOver = false;
        levelStartTime = System.currentTimeMillis();


    }




    public long getElapsedTimeSeconds() {
        return (System.currentTimeMillis() - levelStartTime) / 1000;
    }

    public int getResetTimes() {
        return resetTimes;
    }

    public int getHeroX() {
        return heroX;
    }

    public int getHeroY() {
        return heroY;
    }

    

}
