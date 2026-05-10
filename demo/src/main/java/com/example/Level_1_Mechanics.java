package com.example;

import java.util.List;

public class Level_1_Mechanics {

    private int heroX;
    private int heroY;

    private int keyX;
    private int keyY;

    private int goalX;
    private int goalY;

    private int stars;

   public boolean hasKey = false;

    public Level_1_Mechanics(Level_1_Data level){

        this.heroX = level.heroX;
        this.heroY = level.heroY;

        this.keyX = level.keyX;
        this.keyY = level.keyY;

        this.goalX = level.goalX;
        this.goalY = level.goalY;
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

            case COLLECT:

                if(heroX == keyX && heroY == keyY){
                    hasKey = true;
                    System.out.println("Key collected!");

                }

                break;
        }

    }

    public boolean checkWin(){

        return heroX == goalX && heroY == goalY && hasKey;

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

}