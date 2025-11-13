package com.example;

public class Level {
    private int levelNumber;
    private boolean isCompleted;
    private int starsEarned;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.isCompleted = false;
        this.starsEarned = 0;
    }
    public int getLevelNumber() {
        return levelNumber;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public int getStarsEarned() {
        return starsEarned;
    }
    public void setStarsEarned(int starsEarned) {
        this.starsEarned = starsEarned;
    }
}