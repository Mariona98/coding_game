package com.example;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Applies movement commands to a level, enforces tile/door rules, tracks run
 * state, and reports win, movement, and elapsed-time information to the UI.
 */
public class Level_Mechanics {

    /** Command categories tracked independently; every movement direction shares MOVE. */
    private enum CommandCategory {
        MOVE,
        ATTACK,
        SCAN,
        ADD,
        DROP
    }

    private int heroX;
    private int heroY;
    private final int startX;
    private final int startY;
    private final GameLevelData levelData;
    private String lastMovementMessage;
    private final EnumMap<CommandCategory, Integer> commandUsage =
        new EnumMap<>(CommandCategory.class);
    private boolean commandLimitExceeded;



public boolean dooropened = false;
    public boolean GameOver = false;
    public ArrayList<String> inventory = new ArrayList<>();

    public boolean pressButton = false;
    // Level 1-only temporary and hazard state.
  

    private int resetTimes = 0;
    private long levelStartTime;

    public boolean isGamewon = false;

   

    public Level_Mechanics(GameLevelData levelData) {
        this.levelData = levelData;
        this.startX = levelData.getHeroObjectX();
        this.startY = levelData.getHeroObjectY();
        heroX = this.startX;
        heroY = this.startY;
        levelStartTime = System.currentTimeMillis();
        // Start each category at zero so every level begins with a fresh allowance.
        for (CommandCategory category : CommandCategory.values()) {
            commandUsage.put(category, 0);
        }
    }

    public void executeCommand(Level_Commands cmd) {
        // Invalid/no-op moves leave the hero in place and expose a message to the UI.
        if (GameOver) {
            isGamewon = false;
            System.out.println("Command ignored because the game is over.");
            return;
        }
        if (cmd == null) {
            System.out.println("No command was provided.");
            return;
        }

        lastMovementMessage = null;
        commandLimitExceeded = false;

        // Check the category before running the action so an over-limit command
        // cannot move the hero or change the level state.
        CommandCategory category = getCommandCategory(cmd);
        int used = commandUsage.get(category);
        int limit = category == CommandCategory.MOVE
            ? levelData.getMovementCommandUsageLimit()
            : category == CommandCategory.ATTACK
                ? levelData.getAttackCommandUsageLimit()
                : levelData.getCommandUsageLimit();
        if (used >= limit) {
            commandLimitExceeded = true;
            lastMovementMessage = "Command limit reached: " + getCommandCategoryName(category)
                + " commands can be used at most " + limit + " times in this level."
                + (category == CommandCategory.MOVE
                    ? " All movement directions share this limit."
                    : "");
            System.out.println(lastMovementMessage);
            return;
        }
        commandUsage.put(category, used + 1);

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
            case ATTACK:
                attackEnemy();
                System.out.println("Attack command received.");
                break;
            case SCAN:
                // Scan results are prepared by Python for the active level;
                // scanning itself must not trigger the adjacent-enemy hazard.
                System.out.println("Scan command received.");
                break;  
            case ADD:
                String additem = levelData.getAddItemId(heroX, heroY);
                if (additem == null) {
                    System.out.println("There is no item to pick up at the hero's position.");
                } else {
                    inventory.add(additem);
                    System.out.println("Add command received. Item added to inventory.");
                    System.out.println("Current inventory: " + inventory);
                }
                break;
            case DROP:
                if (!inventory.isEmpty()) {
                    int intex=0;
                    String droppedItem = inventory.get(intex);
                    inventory.remove(intex);
                    System.out.println("Drop command received. Item removed from inventory.");
                    String puzzleId = levelData.getPuzzleObjectIdAt(heroX, heroY);
                    System.out.println("Puzzle ID at hero's position: " + puzzleId);
                    System.out.println("Dropped item: " + droppedItem);
                    if (puzzleId != null && puzzleId.equals(droppedItem)) {
                        System.out.println("Puzzle activated by dropping the correct item.");
                        levelData.setLevelObjectActivatedAt(
                            GameLevelData.LevelObject.Type.PUZZLE,
                            heroX,
                            heroY,
                            true
                        );
                    }
                } else {
                    System.out.println("Drop command received, but inventory is empty.");
                }
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

    /** Groups directional moves together and keeps each other command independent. */
    private CommandCategory getCommandCategory(Level_Commands command) {
        switch (command) {
            case MOVE_RIGHT:
            case MOVE_LEFT:
            case MOVE_UP:
            case MOVE_DOWN:
                return CommandCategory.MOVE;
            case ATTACK:
                return CommandCategory.ATTACK;
            case SCAN:
                return CommandCategory.SCAN;
            case ADD:
                return CommandCategory.ADD;
            case DROP:
                return CommandCategory.DROP;
            default:
                throw new IllegalArgumentException("Unsupported command: " + command);
        }
    }

    /** Provides the player-facing category name used when an allowance is exhausted. */
    private String getCommandCategoryName(CommandCategory category) {
        return category == CommandCategory.MOVE ? "movement" : category.name();
    }
    public void updateButtonState() {
        // Standing on the switch opens the door; stepping away leaves it open.
        if (heroX == levelData.getButtonObjectX() && heroY == levelData.getButtonObjectY()) {
            pressButton = true;
            this.dooropened = true;
            System.out.println("Button pressed!");
        } else if (dooropened) {
            pressButton = false;
            System.out.println("Door is open.");
            
        }
        else{
            pressButton = false;
            this.dooropened = false;
        }
        levelData.setLevelObjectsActivated(GameLevelData.LevelObject.Type.BUTTON, pressButton);
        levelData.setLevelObjectsActivated(GameLevelData.LevelObject.Type.DOOR, dooropened);
    }
  
    public boolean isDoorOpen() {
       
        return this.dooropened;
    }





    public boolean checkWin() {
        boolean reachedOpenDoor = isDoorOpen()
            && heroX == levelData.getDoorX()
            && heroY == levelData.getDoorY();
        boolean hasPuzzle = false;
        boolean allPuzzlesActivated = true;
        boolean hasEnemy = false;
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.PUZZLE) {
                hasPuzzle = true;
                if (!object.isActivated()) {
                    allPuzzlesActivated = false;
                    break;
                }
            }
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                hasEnemy = true;
            }
            if (object.getType() != GameLevelData.LevelObject.Type.ENEMY){
                    hasEnemy = false;
            }
        }

        if (reachedOpenDoor || (hasPuzzle && allPuzzlesActivated)|| (!hasEnemy && !hasPuzzle && !reachedOpenDoor)) {
            setGamewon();
            System.out.println("Game won!");
        }
        return isGamewon;
    }

  




    public void resetGame() {
        // Reset current run state while preserving the total number of resets.
        resetTimes++;

        heroX = startX;
        heroY = startY;
        dooropened = false;
        pressButton = false;
        isGamewon = false;
        levelData.resetLevelObjectActivations();
        lastMovementMessage = null;
        commandLimitExceeded = false;
        for (CommandCategory category : CommandCategory.values()) {
            commandUsage.put(category, 0);
        }
        GameOver = false;
        levelStartTime = System.currentTimeMillis();


    }



    public void enemyAttack() {
        // Check if the hero is on the same tile as any enemy.
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                if (heroX == object.getX() && heroY == object.getY()) {
                    GameOver = true;
                    System.out.println("Game over! The hero was attacked by an enemy.");
                    return;
                }
            }
        }
    }

 public int scanForEnemies() {
         
for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                if (heroX == object.getX() + 1 && heroY == object.getY()) {
                    GameOver = true;
                    System.out.println("Game over! The hero was attacked by an enemy.");
                    return 1;
                }
                if (heroX == object.getX() - 1 && heroY == object.getY()) {
                    GameOver = true;
                    System.out.println("Game over! The hero was attacked by an enemy.");
                    return 2;
                }
                if (heroX == object.getX() && heroY == object.getY() + 1) {
                    GameOver = true;
                    System.out.println("Game over! The hero was attacked by an enemy.");
                    return 3;
                }
                if (heroX == object.getX() && heroY == object.getY() - 1) {
                    GameOver = true;
                    System.out.println("Game over! The hero was attacked by an enemy.");
                    return 4;
                }
            }
        }
        return 0;
    }

    public void attackEnemy() {
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                if ((heroX == object.getX() + 1 && heroY == object.getY()) ||
                    (heroX == object.getX() - 1 && heroY == object.getY()) ||
                    (heroX == object.getX() && heroY == object.getY() + 1) ||
                    (heroX == object.getX() && heroY == object.getY() - 1)) {
                    levelData.removeLevelObject(object);
                    System.out.println("Enemy at (" + object.getX() + ", " + object.getY() + ") has been defeated.");
                    return;
                }
            }
        }
        System.out.println("No enemy adjacent to the hero to attack.");
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

    /** Reports whether the most recent attempted command exceeded its category limit. */
    public boolean didExceedCommandLimit() {
        return commandLimitExceeded;
    }

     public boolean isGamewon() {
        return isGamewon;}

    public void setGamewon() {
        this.isGamewon = true;
    }
}
