package com.example;

import java.util.HashSet;
import java.util.Set;

public class Level_1_Mechanics {

    private int heroX;
    private int heroY;
    private int bug1X;
    private int bug1Y;
    private int bug2X;
    private int bug2Y;
    private int stars;

    public boolean hasKey = false;
    public boolean hasBow = false;
    public boolean hasBoots = false;
    public boolean isInvisible = false;
    public boolean isBug1Dead = false;
    public boolean isBug2Dead = false;
    public boolean isTower1Dead = false;
    public boolean isTower2Dead = false;
    public boolean GameOver = false;
    public boolean chest1Opened = false;
    public boolean chest2Opened = false;

    // Level 1-only temporary and hazard state.
    private final Set<TilePosition> lavaTiles = new HashSet<>();
    private final Set<TilePosition> bridgeTiles = new HashSet<>();

    private int resetTimes = 0;
    private long levelStartTime;

    public enum ArrowDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public Level_1_Mechanics(Level_1_Data level) {
        heroX = level.heroX;
        heroY = level.heroY;
        bug1X = level.bug1X;
        bug1Y = level.bug1Y;
        bug2X = level.bug2X;
        bug2Y = level.bug2Y;

        initialiseLevelOneLava();
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
            case COLLECT:
                collectBow();
                break;
            case OPEN_CHEST:
                openChest();
                break;
            case SHOOT_UP:
                shootArrow(ArrowDirection.UP);
                break;

            case SHOOT_DOWN:
                shootArrow(ArrowDirection.DOWN);
                break;

            case SHOOT_LEFT:
                shootArrow(ArrowDirection.LEFT);
                break;

            case SHOOT_RIGHT:
                shootArrow(ArrowDirection.RIGHT);
                break;
            default:
                System.out.println("Unsupported Level 1 command.");
        }

        if (!moved) {
            return;
        }

        if (!isInsideLevel(heroX, heroY)) {
            heroX = oldX;
            heroY = oldY;
            System.out.println("Out of bounds! The hero stayed at (" + heroX + ", " + heroY + ").");
            return;
        }

        System.out.println("Hero moved to (" + heroX + ", " + heroY + ").");
        safeCheck();

        if (unsafeCheck()) {
            return;
        }

        enemymovment();
        unsafeCheck();
    }

    public boolean checkWin() {
        if (heroX == Level_1_Data.DOOR_X && heroY == Level_1_Data.DOOR_Y) {
            if (hasKey) {
                System.out.println("You completed Level 1 in " + getElapsedTimeSeconds() + " seconds!");
                return true;
            }
            System.out.println("The door is locked. You need the key.");
        }
        return false;
    }

    public void enemymovment() {
        if (!isBug1Dead) {
            moveBug1();
            if (isDangerousLavaTile(bug1X, bug1Y)) {
                isBug1Dead = true;
                System.out.println("Bug 1 moved into lava and was killed.");
            }
        }

        if (!isBug2Dead) {
            moveBug2();
            if (isDangerousLavaTile(bug2X, bug2Y)) {
                isBug2Dead = true;
                System.out.println("Bug 2 moved into lava and was killed.");
            }
        }
    }

    public void safeCheck() {
        if (isLavaTile(heroX, heroY) && hasBoots) {
            System.out.println("The boots protected you from the lava.");
        }
        if (isBridgeTile(heroX, heroY)) {
            System.out.println("You are standing on the bridge.");
        }
    }

    public boolean unsafeCheck() {
        if (!isBug1Dead && bug1X == heroX && bug1Y == heroY) {
            killHero("Game Over! You were killed by Bug 1.");
            return true;
        }

        if (!isBug2Dead && bug2X == heroX && bug2Y == heroY) {
            killHero("Game Over! You were killed by Bug 2.");
            return true;
        }

        if (isDangerousLavaTile(heroX, heroY) && !hasBoots) {
            killHero("Game Over! You stepped into lava.");
            return true;
        }

        if (!isTower1Dead && isAdjacentTo(heroX, heroY, Level_1_Data.TOWER_1_X, Level_1_Data.TOWER_1_Y)) {
            if (isInvisible) {
                System.out.println("Tower 1 could not see you because of the cloak.");
            } else {
                killHero("Game Over! Tower 1 killed you.");
                return true;
            }
        }

        if (!isTower2Dead && isAdjacentTo(heroX, heroY, Level_1_Data.TOWER_2_X, Level_1_Data.TOWER_2_Y)) {
            if (isInvisible) {
                System.out.println("Tower 2 could not see you because of the cloak.");
            } else {
                killHero("Game Over! Tower 2 killed you.");
                return true;
            }
        }

        return false;
    }

    // Collects and removes the bow.
    private void collectBow() {
        if (hasBow) {
            System.out.println("You already collected the bow.");
        } else if (heroX == Level_1_Data.BOW_X && heroY == Level_1_Data.BOW_Y) {
            hasBow = true;
            System.out.println("You collected the bow. It was removed from the map.");
        } else {
            System.out.println("There is no bow at this location.");
        }
    }

    private void openChest() {
        if (heroX == Level_1_Data.CHEST_2_X && heroY == Level_1_Data.CHEST_2_Y) {
            if (chest2Opened) {
                System.out.println("Chest 2 is already empty.");
                return;
            }
            chest2Opened = true;
            hasKey = true;
            System.out.println("You opened Chest 2 and collected the key.");
            return;
        }

        if (heroX == Level_1_Data.CHEST_1_X && heroY == Level_1_Data.CHEST_1_Y) {
            if (chest1Opened) {
                System.out.println("Chest 1 is already empty.");
                return;
            }

            chest1Opened = true;
            if ((int) (Math.random() * 2) == 0) {
                isInvisible = true;
                System.out.println("You opened Chest 1 and collected the invisibility cloak.");
            } else {
                hasBoots = true;
                System.out.println("You opened Chest 1 and collected the lava boots.");
            }
            return;
        }

        System.out.println("There is no chest at this location.");
    }

    public void shootArrow(ArrowDirection direction) {
        if (!hasBow) {
            System.out.println("You cannot shoot because you do not have the bow.");
            return;
        }

        int target = findFirstBugInDirection(direction);
        if (target == 1) {
            isBug1Dead = true;
            System.out.println("You shot Bug 1. Bug 1 was killed instantly.");
        } else if (target == 2) {
            isBug2Dead = true;
            System.out.println("You shot Bug 2. Bug 2 was killed instantly.");
        } else {
            System.out.println("The arrow missed.");
        }
    }

    private int findFirstBugInDirection(ArrowDirection direction) {
        int d1 = getBugDistance(bug1X, bug1Y, isBug1Dead, direction);
        int d2 = getBugDistance(bug2X, bug2Y, isBug2Dead, direction);

        if (d1 == -1 && d2 == -1)
            return 0;
        if (d1 == -1)
            return 2;
        if (d2 == -1)
            return 1;
        return d1 <= d2 ? 1 : 2;
    }

    private int getBugDistance(int x, int y, boolean dead, ArrowDirection direction) {
        if (dead)
            return -1;

        switch (direction) {
            case UP:
                return x == heroX && y < heroY ? heroY - y : -1;
            case DOWN:
                return x == heroX && y > heroY ? y - heroY : -1;
            case LEFT:
                return y == heroY && x < heroX ? heroX - x : -1;
            case RIGHT:
                return y == heroY && x > heroX ? x - heroX : -1;
            default:
                return -1;
        }
    }

    private void moveBug1() {
        if (bug1X < heroX)
            bug1X++;
        else if (bug1X > heroX)
            bug1X--;
        else if (bug1Y < heroY)
            bug1Y++;
        else if (bug1Y > heroY)
            bug1Y--;

        System.out.println("Bug 1 moved to (" + bug1X + ", " + bug1Y + ").");
    }

    private void moveBug2() {
        if (bug2X < heroX)
            bug2X++;
        else if (bug2X > heroX)
            bug2X--;
        else if (bug2Y < heroY)
            bug2Y++;
        else if (bug2Y > heroY)
            bug2Y--;

        System.out.println("Bug 2 moved to (" + bug2X + ", " + bug2Y + ").");
    }

    // Includes horizontal, vertical, and diagonal adjacency.
    private boolean isAdjacentTo(int firstX, int firstY, int secondX, int secondY) {
        int dx = Math.abs(firstX - secondX);
        int dy = Math.abs(firstY - secondY);
        return dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0);
    }

    private boolean isInsideLevel(int x, int y) {
        return x >= 0 && x < Level_1_Data.LEVEL_WIDTH
                && y >= 0 && y < Level_1_Data.LEVEL_HEIGHT;
    }

    private void killHero(String cause) {
        GameOver = true;
        System.out.println(cause);
        System.out.println("You survived for " + getElapsedTimeSeconds() + " seconds.");
    }

    public void resetGame() {
        resetTimes++;

        heroX = Level_1_Data.HERO_START_X;
        heroY = Level_1_Data.HERO_START_Y;
        bug1X = Level_1_Data.BUG_1_START_X;
        bug1Y = Level_1_Data.BUG_1_START_Y;
        bug2X = Level_1_Data.BUG_2_START_X;
        bug2Y = Level_1_Data.BUG_2_START_Y;

        hasKey = false;
        hasBow = false;
        hasBoots = false;
        isInvisible = false;
        isBug1Dead = false;
        isBug2Dead = false;
        isTower1Dead = false;
        isTower2Dead = false;
        chest1Opened = false;
        chest2Opened = false;
        stars = 0;
        GameOver = false;
        levelStartTime = System.currentTimeMillis();

        System.out.println("Level 1 reset. Total resets: " + resetTimes + ".");
        System.out.println("Hero position reset to (" + heroX + ", " + heroY + ").");
        System.out.println("Bug 1 position reset to (" + bug1X + ", " + bug1Y + ").");
        System.out.println("Bug 2 position reset to (" + bug2X + ", " + bug2Y + ").");
        System.out.println(isBug1Dead ? "Bug 1 is dead." : "Bug 1 is alive.");
        System.out.println(isBug2Dead ? "Bug 2 is dead." : "Bug 2 is alive.");
        System.out.println(isTower1Dead ? "Tower 1 is dead." : "Tower 1 is alive.");
        System.out.println(isTower2Dead ? "Tower 2 is dead." : "Tower 2 is alive.");
        System.out.println("Chests opened: " + (chest1Opened ? "Chest 1" : "") + (chest2Opened ? " Chest 2" : "")); 
        System.out.println("Items collected: " + (hasKey ? "Key " : "") + (hasBow ? "Bow " : "") + (hasBoots ? "Boots " : "") + (isInvisible ? "Cloak" : ""));
        System.out.println("Is hero dead? " + (GameOver ? "Yes" : "No"));
    }

    private void initialiseLevelOneLava() {
        addLavaTile(6, 4);
        addLavaTile(7, 4);
        addLavaTile(8, 4);
        addLavaTile(6, 5);
        addLavaTile(8, 5);
        addLavaTile(6, 6);
        addLavaTile(7, 6);
        addLavaTile(8, 6);
        addBridgeTile(6, 5);
    }

    public static class TilePosition {
        private final int x;
        private final int y;

        public TilePosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (!(object instanceof TilePosition))
                return false;
            TilePosition other = (TilePosition) object;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }

    public void addLavaTile(int x, int y) {
        lavaTiles.add(new TilePosition(x, y));
    }

    public boolean isLavaTile(int x, int y) {
        return lavaTiles.contains(new TilePosition(x, y));
    }

    private boolean isDangerousLavaTile(int x, int y) {
        return isLavaTile(x, y) && !isBridgeTile(x, y);
    }

    private void addBridgeTile(int x, int y) {
        bridgeTiles.add(new TilePosition(x, y));
    }

    private boolean isBridgeTile(int x, int y) {
        return bridgeTiles.contains(new TilePosition(x, y));
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

    public int getBug1X() {
        return bug1X;
    }

    public int getBug1Y() {
        return bug1Y;
    }

    public int getBug2X() {
        return bug2X;
    }

    public int getBug2Y() {
        return bug2Y;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public boolean isChest1Opened() {
        return chest1Opened;
    }

    public boolean isChest2Opened() {
        return chest2Opened;
    }
}
