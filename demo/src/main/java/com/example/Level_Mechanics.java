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
        ADD,
        DROP
    }

    private int heroX;
    private int heroY;
    private int totemX;
    private int totemY;
    private boolean totemProgramActive;
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
        totemX = -1;
        totemY = -1;
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.TOTEM) {
                totemX = object.getX();
                totemY = object.getY();
                break;
            }
        }
        levelStartTime = System.currentTimeMillis();
        // Start each category at zero so every level begins with a fresh allowance.
        for (CommandCategory category : CommandCategory.values()) {
            commandUsage.put(category, 0);
        }
    }

    public void executeCommand(Level_Commands cmd) {
        // Invalid/no-op moves leave the actor in place and expose a message to the UI.
        if (cmd == Level_Commands.TOTEM_PROGRAM_END) {
            totemProgramActive = false;
            return;
        }
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

        if (cmd == Level_Commands.TOTEM_PROGRAM) {
            if (totemX < 0 || totemY < 0
                || Math.abs(heroX - totemX) + Math.abs(heroY - totemY) != 1) {
                lastMovementMessage =
                    "The hero must stand next to the totem before calling its function.";
                GameOver = true;
                return;
            }
            totemProgramActive = true;
            return;
        }
        if ((isTotemMovement(cmd) || cmd == Level_Commands.TOTEM_ATTACK)
            && !totemProgramActive) {
            lastMovementMessage =
                "The totem only accepts commands inside a function called with hero.command().";
            GameOver = true;
            return;
        }

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

        if (isTotemMovement(cmd)) {
            moveTotem(cmd);
            return;
        }
        if (cmd == Level_Commands.TOTEM_ATTACK) {
            if (totemX < 0 || totemY < 0) {
                lastMovementMessage = "There is no totem in this level.";
                return;
            }
            attackEnemy(totemX, totemY);
            return;
        }

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
                attackEnemy(heroX, heroY);
                System.out.println("Attack command received.");
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

        enemyAttack();

        System.out.println("Hero moved to (" + heroX + ", " + heroY + ").");
       
    }

    /** Groups directional moves together and keeps each other command independent. */
    private CommandCategory getCommandCategory(Level_Commands command) {
        switch (command) {
            case MOVE_RIGHT:
            case MOVE_LEFT:
            case MOVE_UP:
            case MOVE_DOWN:
            case TOTEM_MOVE_RIGHT:
            case TOTEM_MOVE_LEFT:
            case TOTEM_MOVE_UP:
            case TOTEM_MOVE_DOWN:
                return CommandCategory.MOVE;
            case ATTACK:
            case TOTEM_ATTACK:
                return CommandCategory.ATTACK;
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

    private boolean isTotemMovement(Level_Commands command) {
        return command == Level_Commands.TOTEM_MOVE_RIGHT
            || command == Level_Commands.TOTEM_MOVE_LEFT
            || command == Level_Commands.TOTEM_MOVE_UP
            || command == Level_Commands.TOTEM_MOVE_DOWN;
    }

    private void moveTotem(Level_Commands command) {
        if (totemX < 0 || totemY < 0) {
            lastMovementMessage = "There is no totem in this level.";
            return;
        }
        int nextX = totemX;
        int nextY = totemY;
        switch (command) {
            case TOTEM_MOVE_RIGHT:
                nextX++;
                break;
            case TOTEM_MOVE_LEFT:
                nextX--;
                break;
            case TOTEM_MOVE_UP:
                nextY--;
                break;
            case TOTEM_MOVE_DOWN:
                nextY++;
                break;
            default:
                throw new IllegalArgumentException("Not a totem movement command: " + command);
        }
        if (levelData.getTile(nextX, nextY) == null) {
            lastMovementMessage = levelData.getBoundaryMessage();
            return;
        }
        totemX = nextX;
        totemY = nextY;
        System.out.println("Totem moved to (" + totemX + ", " + totemY + ").");
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
        boolean won;
        switch (levelData.getWinCondition()) {
            case REACH_OPEN_DOOR:
                won = isDoorOpen()
                    && heroX == levelData.getDoorX()
                    && heroY == levelData.getDoorY();
                break;
            case ACTIVATE_ALL_PUZZLES:
                won = hasPuzzles() && areAllPuzzlesActivated();
                break;
            case DEFEAT_ALL_ENEMIES:
                won = areAllEnemiesDefeated();
                break;
            default:
                throw new IllegalStateException(
                    "Unsupported win condition: " + levelData.getWinCondition()
                );
        }

        if (won) {
            setGamewon();
            System.out.println("Game won!");
        }
        return isGamewon;
    }

    private boolean hasPuzzles() {
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.PUZZLE) {
                return true;
            }
        }
        return false;
    }

    private boolean areAllPuzzlesActivated() {
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.PUZZLE
                && !object.isActivated()) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllEnemiesDefeated() {
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                return false;
            }
        }
        return true;
    }

  




    public void resetGame() {
        // Reset current run state while preserving the total number of resets.
        resetTimes++;

        heroX = startX;
        heroY = startY;
        totemProgramActive = false;
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
                    lastMovementMessage = "The hero was attacked by an enemy.";
                    System.out.println("Game over! " + lastMovementMessage);
                    return;
                }
            }
        }
    }

    public void attackEnemy(int attackerX, int attackerY) {
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() == GameLevelData.LevelObject.Type.ENEMY) {
                if ((attackerX == object.getX() + 1 && attackerY == object.getY()) ||
                    (attackerX == object.getX() - 1 && attackerY == object.getY()) ||
                    (attackerX == object.getX() && attackerY == object.getY() + 1) ||
                    (attackerX == object.getX() && attackerY == object.getY() - 1)) {
                    levelData.removeLevelObject(object);
                    System.out.println("Enemy at (" + object.getX() + ", " + object.getY() + ") has been defeated.");
                    return;
                }
            }
        }
        System.out.println("No enemy adjacent to the attacker to attack.");
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

    public int getTotemX() {
        return totemX;
    }

    public int getTotemY() {
        return totemY;
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
