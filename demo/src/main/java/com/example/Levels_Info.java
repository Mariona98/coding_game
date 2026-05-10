package com.example;

import java.util.ArrayList;

public class Levels_Info {

  private Level_1_Mechanics[] levels;//arraylist of levels , added later on?
  private int totalStars;
  private int map;

  
      public Levels_Info() {
          
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
                return levels[levelIndex].getStars();
            }
            return 0;
        }
        public void setLevelStars(int levelIndex, int stars) {
            if (levels != null && levelIndex >= 0 && levelIndex < levels.length) {
                levels[levelIndex].setStars(stars);
            }
        }

}
