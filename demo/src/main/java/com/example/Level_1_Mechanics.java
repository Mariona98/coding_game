package com.example;

import java.util.List;

public class Level_1_Mechanics {

    private int heroX;
    private int heroY;

    private int bowX;
    private int bowY;
    
    private int tower1X;
    private int tower1Y;

    private int tower2X;
    private int tower2Y;

    private int chest1X;
    private int chest1Y;

    private int chest2X;
    private int chest2Y;

    private int bug1X;
    private int bug1Y;

    private int bug2X;
    private int bug2Y;

    private int doorX;
    private int doorY;

    private int stars;

   public boolean hasKey = false;

   public boolean hasBow = false;

   public boolean hasBoots = false;

   public boolean isInvisible = false;

    public Level_1_Mechanics(Level_1_Data level){

        this.heroX = level.heroX;
        this.heroY = level.heroY;

        this.tower1X = level.tower1X;
        this.tower1Y = level.tower1Y;

        this.tower2X = level.tower2X;
        this.tower2Y = level.tower2Y;

        this.bowX = level.bowX;
        this.bowY = level.bowY;

        this.chest1X = level.chest1X;
        this.chest1Y = level.chest1Y;

        this.chest2X = level.chest2X;
        this.chest2Y = level.chest2Y;

        this.bug1X = level.bug1X;
        this.bug1Y = level.bug1Y;

        this.bug2X = level.bug2X;
        this.bug2Y = level.bug2Y;

        this.doorX = level.doorX;
        this.doorY = level.doorY;

    }

    public void executeCommand(Level_1_Commands cmd){

        switch(cmd){

            case MOVE_RIGHT:
                heroX++;
                break;

            case MOVE_LEFT:
                heroX--;
                break;

            case MOVE_UP:
                heroY--;
                break;

            case MOVE_DOWN:
                heroY++;
                break;

            case OPEN_CHEST:

            if (heroX == chest2X && heroY == chest2Y){ 
                hasKey = true;
                System.out.println("Key collected from chest2!");
            }
            if (heroX == chest1X && heroY == chest1Y){
                int randomNum = (int)(Math.random() * 2); 
                if (randomNum == 0) {
                    isInvisible = true;
                    System.out.println("Invisibility cloak collected from chest1!");
                } else if (randomNum == 1) {
                    hasBoots = true;
                    System.out.println("Boots collected from chest1!");
                }
                        }
            else {System.out.println("No chest at this location to open.");   }
                break;
        }
    }

    public boolean checkWin(){

        return heroX == doorX && heroY == doorY && hasKey;

    }

    public int getHeroX(){
        return heroX;
    }

    public int getHeroY(){
        return heroY;
    }

    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }

    public void enemymovment(){

        if (bug1X < heroX) {
            bug1X++;
        } else if (bug1X > heroX) {
            bug1X--;
        }

        if (bug1Y < heroY) {
            bug1Y++;
        } else if (bug1Y > heroY) {
            bug1Y--;
        }

        if (bug2X < heroX) {
            bug2X++;
        } else if (bug2X > heroX) {
            bug2X--;
        }

        if (bug2Y < heroY) {
            bug2Y++;
        } else if (bug2Y > heroY) {
            bug2Y--;
        }
    }

}