package com.example;

import java.util.ArrayList;

public class GameStatus {

  private Level[] levels;//arraylist of levels , added later on?
  private int totalStars;
  private int map;

  
      public GameStatus() {
          
          this.totalStars = 0 ;
          this.map = 1;
      }

        public int getTotalStars() {
            return totalStars;
        }
        public void setTotalStars(int totalStars) {
            this.totalStars = totalStars;
        }
        public int getMap() {
            return map;
        }
        public void setMap(int map) {
            this.map = map;
        }
        public int getLevelStars(int levelIndex) {
            if (levels != null && levelIndex >= 0 && levelIndex < levels.length) {
                return levels[levelIndex].getStarsEarned();
            }
            return 0;
        }


}
