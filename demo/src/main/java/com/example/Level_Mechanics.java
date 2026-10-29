package com.example;

/**
 * Applies movement commands to a level, enforces tile/door rules, tracks run
 * state, and reports win, movement, and elapsed-time information to the UI.
 */
public class Level_Mechanics {

    private int heroX;
    private int heroY;
    private final int startX;
    private final int startY;
    private final GameLevelData levelData;
    private String lastMovementMessage;

public boolean dooropened = false;
    public boolean GameOver = false;

    public boolean pressButton = false;
    // Level 1-only temporary and hazard state.
  

    private int resetTimes = 0;
    private long levelStartTime;

   

    public Level_Mechanics(GameLevelData levelData) {
        this.levelData = levelData;
        this.startX = levelData.getHeroStartX();
        this.startY = levelData.getHeroStartY();
        heroX = this.startX;
        heroY = this.startY;
        levelStartTime = System.currentTimeMillis();
    }

    public void executeCommand(Level_Commands cmd) {
        // Invalid/no-op moves leave the hero in place and expose a message to the UI.
        if (GameOver) {
            System.out.println("Command ignored because the game is over.");
            return;
        }
        if (cmd == null) {
            System.out.println("No command was provided.");
            return;
        }

        lastMovementMessage = null;
        int nextX = heroX;
        int nextY = heroY;

        switch (cmd) {
            case MOVE_RIGHT:
                nextX++;
                break;

            case MOVE_LEFT:
                nextX--;
                break;

            case MOVE_UP:
                nextY--;
                break;

            case MOVE_DOWN:
                nextY++;
                break;

            default:
                System.out.println("Unsupported Level 1 command.");
        }

        if (nextX == heroX && nextY == heroY) {
            return;
        }

        GameLevelData.Tile tile = levelData.getTile(nextX, nextY);
        if (tile == null) {
            lastMovementMessage = levelData.getBoundaryMessage();
            return;
        }
        GameLevelData.LevelObject blockingObject =
            levelData.getBlockingObjectAt(nextX, nextY);
        if (blockingObject != null) {
            lastMovementMessage = blockingObject.getBlockedMessage() == null
                ? "That tile is blocked."
                : blockingObject.getBlockedMessage();
            return;
        }
        if (!tile.isWalkable()) {
            lastMovementMessage =  "That tile is blocked.";
            return;
        }
        if (nextX == levelData.getDoorX()
            && nextY == levelData.getDoorY()
            && !dooropened) {
            lastMovementMessage = "The door is closed. Find and press the switch first.";
            return;
        }

        heroX = nextX;
        heroY = nextY;
        

        System.out.println("Hero moved to (" + heroX + ", " + heroY + ").");
       
    }
    public void updateButtonState() {
        // Standing on the switch opens the door; stepping away leaves it open.
        if (heroX == levelData.getButtonX() && heroY == levelData.getButtonY()) {
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
        if (isDoorOpen()
            && heroX == levelData.getDoorX()
            && heroY == levelData.getDoorY()) {
            System.out.println("Hero reached the open door. Level completed!");
            return true;
           
        }
        return false;
    }

    public boolean checkHeroDead() {
        return GameOver;
    }




    public void resetGame() {
        // Reset current run state while preserving the total number of resets.
        resetTimes++;

        heroX = startX;
        heroY = startY;
        dooropened = false;
        pressButton = false;
        lastMovementMessage = null;
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

    public String getLastMovementMessage() {
        return lastMovementMessage;
    }

}
